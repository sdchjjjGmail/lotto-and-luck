package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.domain.interactor.GetCachedDrawingResultUseCase
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.presenter.model.AnalyzeResult
import com.content.lottoandluck.presenter.viewmodel.sharedstate.StatisticsState
import com.content.lottoandluck.utils.LatestRound
import com.content.lottoandluck.utils.NumberAnalyzer
import com.content.lottoandluck.utils.NumberAnalyzer.getBonusCount
import com.content.lottoandluck.utils.NumberAnalyzer.getLastPop
import com.content.lottoandluck.utils.NumberAnalyzer.getPopPercentage
import com.content.lottoandluck.utils.NumberAnalyzer.getTotalCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class StatisticsViewModel(
    private val getCachedDrawingResultUseCase: GetCachedDrawingResultUseCase
) {
    val showDetail = mutableStateOf(false)
    val selectedNumber = mutableStateOf("")
    val totalPopPercentage = mutableStateOf("")
    val totalBonusPopPercentage = mutableStateOf("")
    val lastPopState = mutableStateOf("")
    val skipRoundPattern = mutableStateOf("")

    private val cachedDrawingResultMap = HashMap<Int, Drawing>()
    private var popCount = 0
    private var bonusPopCount = 0
    private var lastPop = 0

    init {
        setDrawingResultMap()
    }

    private fun setDrawingResultMap() {
        if (cachedDrawingResultMap.isEmpty() || cachedDrawingResultMap.keys.last() < LatestRound.get()
                .toInt()
        ) {
            cachedDrawingResultMap.clear()
            CoroutineScope(Dispatchers.Default).launch {
                getCachedDrawingResultUseCase.invoke()
                    .onStart { }
                    .catch { it.printStackTrace() }
                    .collect { result ->
                        cachedDrawingResultMap.putAll(result)
                    }
            }
        }
    }

    fun onNumberClicked(number: Int) {
        popCount = getTotalCount(number, cachedDrawingResultMap)
        bonusPopCount = getBonusCount(number, cachedDrawingResultMap)
        lastPop = getLastPop(number, cachedDrawingResultMap)
        selectedNumber.value = number.toString()
        lastPopState.value = getLastPopText(lastPop)
        totalPopPercentage.value =
            "${popCount}회 출현(${
                getPopPercentage(
                    popCount.toDouble(),
                    LatestRound.get().toDouble()
                )
            }%)"
        totalBonusPopPercentage.value =
            "보너스 ${bonusPopCount}회 출현(${
                getPopPercentage(
                    bonusPopCount.toDouble(),
                    LatestRound.get().toDouble()
                )
            }%)"
        getStatistics(number)
    }

    private fun getStatistics(number: Int) {
        NumberAnalyzer.getDrawingPattern(number, cachedDrawingResultMap, false)
            .also {
                if (!it.keys.contains(lastPop + 1)) {
                    skipRoundPattern.value = "${lastPop + 1} 회차 이후 출현 정보가 없어요."
                    return
                }
            }
            .forEach { (key, value) ->
                if (key == lastPop + 1) {
                    with(
                        AnalyzeResult(
                            skipRound = key,
                            skipRoundText = getSkipRoundText(key),
                            skipRoundCount = "${value}번",
                            popPercentage = "(${
                                getPopPercentage(
                                    value.toDouble(),
                                    popCount.toDouble()
                                )
                            }%)",
                            lastPop = lastPop
                        )
                    ) {
                        skipRoundPattern.value =
                            "$skipRoundText $skipRoundCount$popPercentage"
                    }
                }
            }
    }

    private fun getLastPopText(number: Int): String {
        return if (number == 0) {
            "마지막 출현: 최신 회차(${LatestRound.get()}회)"
        } else {
            "마지막 출현: ${number}회차 전"
        }
    }

    private fun getSkipRoundText(number: Int): String {
        return if (number == 1) {
            "연속출현 횟수:"
        } else {
            "${number}회차 이후 출현 횟수:"
        }
    }

    fun showMore() {
        showDetail.value = !showDetail.value
    }

    fun clearSelectedNumber() {
        showDetail.value = false
        selectedNumber.value = ""
        totalPopPercentage.value = ""
        totalBonusPopPercentage.value = ""
        lastPopState.value = ""
        skipRoundPattern.value = ""
    }

    fun exit() {
        showDetail.value = false
        StatisticsState.exit()
    }
}