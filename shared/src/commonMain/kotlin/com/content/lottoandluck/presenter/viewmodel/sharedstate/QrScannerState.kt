package com.content.lottoandluck.presenter.viewmodel.sharedstate

import androidx.compose.runtime.mutableStateOf

object QrScannerState {
    val showQrScanner = mutableStateOf(false)

    fun showQrScanner() {
        showQrScanner.value = true
    }

    fun closeQrScanner() {
        showQrScanner.value = false
    }
}