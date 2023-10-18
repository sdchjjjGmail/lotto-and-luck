package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateOf


class MainViewModel {
    val currentScreen = mutableStateOf((0))

    fun setScreen(screenNumber: Int) {
        if (currentScreen.value == screenNumber) return
        currentScreen.value = screenNumber
    }
}
