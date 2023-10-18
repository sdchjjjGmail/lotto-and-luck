package com.content.lottoandluck.nativespecific.qr

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import toCommonStateFlow

open class QrScannerStateViewModel(
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _qrScannerState = MutableStateFlow(QrScannerState())
    val qrScannerState = _qrScannerState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = QrScannerState()
        )
        .toCommonStateFlow()

    fun onEvent(event: QrScannerEvent) {
        when (event) {
            is QrScannerEvent.OpenQrScreenScanner -> {
                _qrScannerState.update {
                    it.copy(openQrScanner = true)
                }
            }

            is QrScannerEvent.CloseQrScreenScanner -> {
                _qrScannerState.update {
                    it.copy(openQrScanner = false)
                }
            }
        }
    }
}