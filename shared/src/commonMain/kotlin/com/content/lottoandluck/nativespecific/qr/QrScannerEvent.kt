package com.content.lottoandluck.nativespecific.qr

sealed class QrScannerEvent {
    object OpenQrScreenScanner : QrScannerEvent()
    object CloseQrScreenScanner : QrScannerEvent()
}
