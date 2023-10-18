package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.domain.interactor.GetCachedDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetLastRoundUseCase
import com.content.lottoandluck.domain.interactor.SaveLotteryNumberUseCase
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.StatisticsState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.ToastMessageState
import com.content.lottoandluck.utils.LatestRound
import com.content.lottoandluck.utils.NumberAnalyzer
import com.content.lottoandluck.utils.NumberAnalyzer.getMultipleSerialPopOfSingleNumberCount
import com.content.lottoandluck.utils.NumberAnalyzer.getSerialPop
import com.content.lottoandluck.utils.NumberAnalyzer.getSerialPopPercentage
import com.content.lottoandluck.utils.RandomGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecommendationViewModel(
    private val getLastRoundUseCase: GetLastRoundUseCase,
    private val getCachedDrawingResultUseCase: GetCachedDrawingResultUseCase,
    private val saveLotteryNumberUseCase: SaveLotteryNumberUseCase

) {
    val simulatedNumberState = mutableStateListOf<Int>()
    val isGenerated = mutableStateOf(false)

    private val cachedDrawingResultMap = HashMap<Int, Drawing>()
    private val decimalPlaceDuplicateMap = HashMap<Int, HashMap<Int, Int>>()
    private val serialNumberCountMap = HashMap<Int, Int>()
    private val multipleSerialPopOfSingleNumberCountMap = HashMap<Int, HashMap<Int, Int>>()

    private val singleNumberPercentageList = ArrayList<Double>()
    private val numberPlacePercentageList = ArrayList<Double>()

    private var lastRound = 1
    private val onesMap = HashMap<Int, Int>()
    private val tensMap = HashMap<Int, Int>()
    private val twentiesMap = HashMap<Int, Int>()
    private val thirtiesMap = HashMap<Int, Int>()
    private val fortiesMap = HashMap<Int, Int>()
    private var numberSerialPercentage: Double = 0.0
    private val singleNumberLastSerialPopPercentageList = ArrayList<Double>()
    private val simulatedNumber = ArrayList<Int>()

    fun init() {
        if (isReady()) {
            setCachedResults()
        }
    }

    private fun isReady(): Boolean {
        return DrawingDownloadState.ready.value
    }

    private fun requireDownload() {
        DrawingDownloadState.showDownloadScreen()
        DrawingDownloadState.showDownloadPopup(DrawingDownloadState.lastRound.value)
    }

    private fun setCachedResults() {
        if (cachedDrawingResultMap.isEmpty() || cachedDrawingResultMap.keys.last() < LatestRound.get()
                .toInt()
        ) {
            cachedDrawingResultMap.clear()
            CoroutineScope(Dispatchers.Default).launch {
                getLastRoundUseCase.invoke()
                    .catch { it.printStackTrace() }
                    .collect {
                        lastRound = it.toInt()
                    }
                getCachedDrawingResultUseCase.invoke()
                    .onStart { }
                    .catch { it.printStackTrace() }
                    .collect { result ->
                        cachedDrawingResultMap.putAll(result)
                        setPlaceAnalysis()
                        setSerialPopAnalysis()
                        setSingleNumberSerialPopCountMap()
                    }
            }
        }
    }

    private fun setPlaceAnalysis() {
        for (i in 1..lastRound) {
            with(NumberAnalyzer.getNumberPlaceDuplicateCount(cachedDrawingResultMap[i]!!.numberList)) {
                if (get(1) != null) {
                    onesMap[get(1)!!] = getMapValueOrDefault(onesMap, get(1)!!).plus(1)
                    decimalPlaceDuplicateMap[1] = onesMap
                }
                if (get(10) != null) {
                    tensMap[get(10)!!] = getMapValueOrDefault(tensMap, get(10)!!).plus(1)
                    decimalPlaceDuplicateMap[10] = tensMap
                }
                if (get(20) != null) {
                    twentiesMap[get(20)!!] = getMapValueOrDefault(twentiesMap, get(20)!!).plus(1)
                    decimalPlaceDuplicateMap[20] = twentiesMap
                }
                if (get(30) != null) {
                    thirtiesMap[get(30)!!] = getMapValueOrDefault(thirtiesMap, get(30)!!).plus(1)
                    decimalPlaceDuplicateMap[30] = thirtiesMap
                }
                if (get(40) != null) {
                    fortiesMap[get(40)!!] = getMapValueOrDefault(fortiesMap, get(40)!!).plus(1)
                    decimalPlaceDuplicateMap[40] = fortiesMap
                }
            }
        }
    }

    private fun setSerialPopAnalysis() {
        NumberAnalyzer.getSerialDrawingCountMap(cachedDrawingResultMap).forEach { (key, value) ->
            serialNumberCountMap[key] = value
        }
    }

    private fun setSingleNumberSerialPopCountMap() {
        getMultipleSerialPopOfSingleNumberCount(cachedDrawingResultMap).forEach { (key, value) ->
            multipleSerialPopOfSingleNumberCountMap[key] = value
        }
    }

    private fun getMapValueOrDefault(map: HashMap<Int, Int>, key: Int): Int {
        return if (map[key] == null) {
            0
        } else {
            map[key]!!
        }
    }

    fun startSimulation() {
        SavedNumberManagerPopupState.hidePopup()
        if (!isReady()) {
            requireDownload()
            return
        }
        isGenerated.value = true
        var minPercentage = 2.00
        val generatedNumberList = ArrayList<Int>()
        do {
            do {
                generatedNumberList.clear()
                generatedNumberList.addAll(drawRandom())
            } while (exist(generatedNumberList))
            setSingleNumberStatistics(generatedNumberList)
            setNumberPlaceStatistics(generatedNumberList)
            setNumberSerialPopStatistics(generatedNumberList)
            getLastSerialPopStatistics(generatedNumberList)
        } while (!passed())
        simulatedNumberState.clear()
        simulatedNumberState.addAll(generatedNumberList.sorted())
            .also {
                if (it) {
                    simulatedNumber.clear()
                    simulatedNumber.addAll(generatedNumberList.sorted())
                }
            }
    }

    private fun getScore(): Int {
        var score = 0.00
        singleNumberPercentageList.forEach {
            score += it / 10
        }
        numberPlacePercentageList.forEach {
            score += it / 10
        }
        score += numberSerialPercentage / 10
        return score.toInt()
    }

    private fun exist(myList: ArrayList<Int>): Boolean {
        cachedDrawingResultMap.forEach { (key) ->
            if (cachedDrawingResultMap[key]!!.numberList == myList.sorted()) return true
        }
        return false
    }

    private fun setSingleNumberStatistics(generatedNumberList: ArrayList<Int>) {
        singleNumberPercentageList.clear()
        generatedNumberList.forEach {
            singleNumberPercentageList.add(getSingleNumberPercentage(it))
        }
    }

    //다음 회차에 출현 확률 계산
    private fun getSingleNumberPercentage(
        number: Int,
    ): Double {
        val popCount = NumberAnalyzer.getTotalCount(number, cachedDrawingResultMap)
        val lastPop = NumberAnalyzer.getLastPop(number, cachedDrawingResultMap)
        with(
            NumberAnalyzer.getDrawingPattern(number, cachedDrawingResultMap, false)
        ) {
            return NumberAnalyzer.getPopPercentage(
                getMapValueOrDefault(
                    this@with,
                    lastPop + 1
                ).toDouble(),
                popCount.toDouble()
            ).toDouble()
        }
    }

    private fun setNumberPlaceStatistics(generatedNumberList: ArrayList<Int>) {
        numberPlacePercentageList.clear()
        with(NumberAnalyzer.getNumberPlaceDuplicateCount(generatedNumberList)) {
            forEach { (key, value) ->
                if (decimalPlaceDuplicateMap[key]!![value] != null) {
                    numberPlacePercentageList.add(
                        NumberAnalyzer.getNumberPlacePercentage(
                            decimalPlaceDuplicateMap[key]!![value]!!.toDouble(),
                            lastRound.toDouble()
                        ).toDouble()
                    )
                } else {
                    numberPlacePercentageList.add(0.0)
                }
            }
        }
    }

    private fun setNumberSerialPopStatistics(generatedNumberList: ArrayList<Int>) {
        val serialCount = NumberAnalyzer.getSerialCount(
            cachedDrawingResultMap[lastRound]!!.numberList,
            generatedNumberList
        )
        numberSerialPercentage = NumberAnalyzer.getSerialCountPercentage(
            serialNumberCountMap[serialCount]!!.toDouble(),
            lastRound.toDouble()
        ).toDouble()
    }

    private fun getLastSerialPopStatistics(generatedNumberList: ArrayList<Int>) {
        singleNumberLastSerialPopPercentageList.clear()
        generatedNumberList.forEach {
            if (getSerialPop(it, cachedDrawingResultMap, lastRound) != 0) {
                with(
                    getSerialPopPercentage(
                        multipleSerialPopOfSingleNumberCountMap[it]!![getSerialPop(
                            it,
                            cachedDrawingResultMap,
                            lastRound
                        )]!!.toDouble(),
                        lastRound.toDouble()
                    )
                ) {
                    singleNumberLastSerialPopPercentageList.add(this.toDouble())
                }
            }
        }
    }

    private fun passed(): Boolean {
        numberPlacePercentageList.forEach {
            if (it < 10.00) {
                return false
            }
        }
        if (numberSerialPercentage < 15.00) {
            return false
        }
        if (singleNumberLastSerialPopPercentageList.isNotEmpty()) {
            singleNumberLastSerialPopPercentageList.forEach {
                if (it < 7.00) {
                    return false
                }
            }
        }
        return true
    }

    private fun drawRandom(): List<Int> {
        isGenerated.value = true
        val minPercentage = 2.00
        with(ArrayList<Int>()) {
            while (this.size < 6) {
                with(RandomGenerator.generate(1, 45)) {
                    if (!contains(this)) {
                        if (getSingleNumberPercentage(this) > minPercentage) {
                            add(this)
                        }
                    }
                }
            }
            return this@with
        }
    }

    fun askSave() {
        SavedNumberManagerPopupState.saveAskPopup()
    }

    fun save() {
        ToastMessageState.show()
        CoroutineScope(Dispatchers.Default).launch {
            saveLotteryNumberUseCase(
                LotteryNumber(
                    numberList = simulatedNumberState
                )
            )
        }
    }

    fun showStatistics() {
        StatisticsState.show(listOf(simulatedNumber))
    }
}