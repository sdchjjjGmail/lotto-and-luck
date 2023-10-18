package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.domain.interactor.SaveLotteryNumberUseCase
import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.ToastMessageState
import com.content.lottoandluck.utils.RandomGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RandomViewModel(
    private val saveLotteryNumberUseCase: SaveLotteryNumberUseCase
) {
    val generatedListState = mutableStateListOf<List<Int>>()
    val isGenerated = mutableStateOf(false)
    val dropDownExpanded = mutableStateOf(false)
    val generateCount = mutableStateOf(1)

    private val generatedList = ArrayList<List<Int>>()

    fun reset() {
        isGenerated.value = false
        generatedListState.clear()
        SavedNumberManagerPopupState.hidePopup()
    }

    fun dropDownExpand() {
        dropDownExpanded.value = !dropDownExpanded.value
    }

    fun closeExpand() {
        dropDownExpanded.value = false
    }

    fun countSelected(count: Int) {
        reset()
        generateCount.value = count
        dropDownExpanded.value = false
    }

    fun generateRandomNumber() {
        closeExpand()
        isGenerated.value = true
        generatedList.clear()
        CoroutineScope(Dispatchers.Default).launch {
            for (i in 1..generateCount.value) {
                with(ArrayList<Int>()) {
                    while (this.size < 6) {
                        with(RandomGenerator.generate(1, 45)) {
                            if (!contains(this)) {
                                add(this)
                            }
                        }
                    }
                    generatedList.add(sorted())
                }
            }
            runBlocking(Dispatchers.Main) {
                launch {
                    generatedListState.clear()
                    generatedListState.addAll(generatedList)
                }
            }
        }
    }

    fun askSave() {
        dropDownExpanded.value = false
        SavedNumberManagerPopupState.saveAskPopup()
    }

    fun save() {
        ToastMessageState.show()
        CoroutineScope(Dispatchers.Default).launch {
            for (singleLottery: List<Int> in generatedListState) {
                saveLotteryNumberUseCase(
                    LotteryNumber(
                        singleLottery
                    )
                )
            }
        }
    }
}