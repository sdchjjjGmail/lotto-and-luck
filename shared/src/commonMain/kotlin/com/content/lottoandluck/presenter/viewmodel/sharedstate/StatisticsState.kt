package com.content.lottoandluck.presenter.viewmodel.sharedstate

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

object StatisticsState {
    val show = mutableStateOf(false)
    val numbers = mutableStateListOf<List<Int>>()
    val pageCount = mutableStateOf(1)

    fun show(numberList: List<List<Int>>) {
        if (numberList.isEmpty()) return
        with(ArrayList<List<Int>>()) {
            for (singleList: List<Int> in numberList) {
                add(singleList)
            }
            numbers.clear()
            numbers.addAll(this@with)
            pageCount.value = count()
        }
        show.value = true
    }

    fun exit() {
        show.value = false
    }
}