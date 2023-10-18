package com.content.lottoandluck.presenter.viewmodel.sharedstate

import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.utils.LatestRound

object DrawingDownloadState {
    val showDownloadScreen = mutableStateOf(true)
    val ready = mutableStateOf(false)
    val showPopup = mutableStateOf(false)
    val initial = mutableStateOf(true)
    val lastRound = mutableStateOf(1)

    fun showDownloadScreen() {
        showDownloadScreen.value = true
    }

    fun closeDownloadScreen() {
        showDownloadScreen.value = false
    }

    fun ready() {
        ready.value = true
    }

    fun notReady() {
        ready.value = false
    }

    fun setLastRound(round: Int) {
        lastRound.value = round
    }

    fun showDownloadPopup(latestDownloaded: Int) {
        showPopup.value = true
        initial.value = isInitial(latestDownloaded)
    }

    fun hideDownloadPopup() {
        showPopup.value = false
    }

    private fun isInitial(latestDownloaded: Int): Boolean {
        return (LatestRound.get().toInt() - latestDownloaded) > 1
    }
}