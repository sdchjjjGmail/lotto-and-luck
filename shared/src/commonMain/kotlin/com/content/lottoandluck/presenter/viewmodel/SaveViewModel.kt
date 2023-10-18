package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.domain.interactor.DeleteSavedLotteriesUseCase
import com.content.lottoandluck.domain.interactor.DeleteSavedLotteryUseCase
import com.content.lottoandluck.domain.interactor.GetSavedLotteryUseCase
import com.content.lottoandluck.domain.model.SavedLotteryNumber
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.StatisticsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SaveViewModel(
    private val getSavedLotteryUseCase: GetSavedLotteryUseCase,
    private val deleteSavedLotteryUseCase: DeleteSavedLotteryUseCase,
    private val deleteSavedLotteriesUseCase: DeleteSavedLotteriesUseCase,
) {
    val savedNumberListState = mutableStateListOf<SavedLotteryNumber>()
    val selectionMapState = mutableStateMapOf<String, Boolean>()
    private val deleteAll = mutableStateOf(false)
    val selectedCount = mutableStateOf(0)
    val loading = mutableStateOf(false)

    private val selectedNumberList = ArrayList<SavedLotteryNumber>()

    init {
        getSavedNumbers()
    }

    private fun isReady(): Boolean {
        return DrawingDownloadState.ready.value
    }

    private fun requireDownload() {
        DrawingDownloadState.showDownloadScreen()
        DrawingDownloadState.showDownloadPopup(DrawingDownloadState.lastRound.value)
    }

    private fun getSavedNumbers() {
        CoroutineScope(Dispatchers.Default).launch {
            getSavedLotteryUseCase()
                .onStart {
                    runBlocking(Dispatchers.Main) {
                        loading.value = true
                    }
                }
                .catch {
                    it.printStackTrace()
                    runBlocking(Dispatchers.Main) {
                        loading.value = false
                    }
                }
                .collect {
                    runBlocking(Dispatchers.Main) {
                        launch {
                            savedNumberListState.clear()
                            savedNumberListState.addAll(it)
                        }
                        loading.value = false
                    }
                }
        }
    }

    fun unselectAll() {
        SavedNumberManagerPopupState.hidePopup()
        clearSelectionMap()
        selectedNumberList.clear()
        selectedCount.value = 0
    }

    fun numberSelected(number: SavedLotteryNumber) {
        SavedNumberManagerPopupState.hidePopup()
        with(selectedNumberList) {
            if (contains(number)) {
                remove(number)
                setSelectionMap(number.id)
            } else {
                add(number)
                setSelectionMap(number.id)
            }
            selectedCount.value = count()
        }
    }

    private fun setSelectionMap(key: String) {
        selectionMapState[key] = !(selectionMapState[key] ?: false)
    }

    private fun clearSelectionMap() {
        selectionMapState.clear()
    }

    fun showStatistics() {
        if (!isReady()) {
            requireDownload()
            return
        }
        with(ArrayList<List<Int>>()) {
            for (singleNumber: SavedLotteryNumber in selectedNumberList) {
                add(singleNumber.numberList)
            }
            StatisticsState.show(this@with)
            unselectAll()
        }
    }

    fun askDelete() {
        if (selectedNumberList.isNotEmpty()) {
            deleteAll.value = false
            SavedNumberManagerPopupState.deleteAskPopup("삭제")
        }
    }

    fun askDeleteAll() {
        deleteAll.value = true
        SavedNumberManagerPopupState.deleteAskPopup("모두 삭제")
    }

    fun delete() {
        if (deleteAll.value) {
            deleteSavedLotteries()
        } else {
            deleteSelectedLottery()
        }
    }

    private fun deleteSavedLotteries() {
        CoroutineScope(Dispatchers.Default).launch {
            deleteSavedLotteriesUseCase.invoke()
        }
        unselectAll()
    }

    private fun deleteSelectedLottery() {
        CoroutineScope(Dispatchers.Default).launch {
            for (singleItem: SavedLotteryNumber in selectedNumberList) {
                deleteSavedLotteryUseCase.invoke(singleItem.id)
            }
            unselectAll()
        }
    }
}