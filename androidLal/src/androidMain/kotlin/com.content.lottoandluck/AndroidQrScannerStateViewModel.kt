package com.content.lottoandluck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.content.lottoandluck.nativespecific.qr.QrScannerEvent
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel

class AndroidQrScannerStateViewModel : ViewModel() {
    val viewModel by lazy {
        QrScannerStateViewModel(
            coroutineScope = viewModelScope
        )
    }

    val qrScannerState = viewModel.qrScannerState
}