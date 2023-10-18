package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.data.model.mapper.mapRowDrawingResultToCoreData
import com.content.lottoandluck.domain.interactor.GetCachedDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetDrawingResultUseCase
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.utils.DropDownDrawingRoundCalculator
import com.content.lottoandluck.utils.LatestRound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchViewModel(
    private val getCachedDrawingResultUseCase: GetCachedDrawingResultUseCase,
    private val getDrawingResultUseCase: GetDrawingResultUseCase
) {
    val searchResult = mutableStateOf(Drawing())
    val resultDisplay = mutableStateOf("")
    val dropDownExpanded = mutableStateOf(false)
    val dropDownItemList = mutableStateListOf<Int>()

    private val cachedDrawingResultMap = HashMap<Int, Drawing>()

    fun init() {
        if (isReady()) {
            getCachedResults()
        }
    }

    private fun isReady(): Boolean {
        return DrawingDownloadState.ready.value
    }

    fun dropDownExpand() {
        dropDownExpanded.value = !dropDownExpanded.value
    }

    fun closeDropDown() {
        dropDownExpanded.value = false
    }

    fun search(round: String) {
        with(filterSearchText(round)) {
            if (DrawingDownloadState.ready.value) {
                setSearchResult(getMapOrDefault(this@with))
            } else {
                requestResult(toInt())
            }
        }
    }

    private fun filterSearchText(text: String): String {
        return if (text == "") {
            LatestRound.get()
        } else {
            text
        }
    }

    private fun setDropDownList(round: Int) {
        with(ArrayList<Int>()) {
            with(LatestRound.get().toInt()) {
                DropDownDrawingRoundCalculator.getList(
                    getValidRound(
                        round,
                        this
                    ), this
                ).forEach {
                    add(it)
                }
            }
            dropDownItemList.clear()
            dropDownItemList.addAll(this@with)
        }
    }

    private fun getValidRound(selectedRound: Int, latestRound: Int): Int {
        return if (selectedRound > latestRound) {
            latestRound
        } else {
            selectedRound
        }
    }

    private fun getCachedResults() {
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
                        if (result.isNotEmpty()) {
                            setSearchResult(
                                getMapOrDefault(
                                    result.keys.last().toString()
                                )
                            )
                        }
                    }
            }
        }
    }

    private fun getMapOrDefault(key: String): Drawing {
        with(cachedDrawingResultMap[key.toInt()]) {
            if (this@with != null) {
                return this@with
            } else {
                return Drawing()
            }
        }
    }

    private fun requestResult(round: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            getDrawingResultUseCase.invoke(round)
                .collect {
                    runBlocking(Dispatchers.Main) {
                        if (it.returnValue == "success") {
                            setSearchResult(mapRowDrawingResultToCoreData(it))
                        } else {
                            setSearchResult(Drawing())
                        }
                    }
                }
        }
    }

    private fun setSearchResult(drawing: Drawing) {
        resultDisplay.value = if (drawing.round == -1) {
            "회차를 찾을 수 없어요"
        } else {
            "${drawing.round}회 (${drawing.date})"
        }
        searchResult.value = drawing
        setDropDownList(drawing.round)
    }
}