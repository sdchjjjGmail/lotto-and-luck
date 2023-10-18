package com.content.lottoandluck

import MainView
import QrScanScreen
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.content.lottoandluck.di.initKoin
import com.content.lottoandluck.nativespecific.qr.QrScannerState
import org.koin.core.KoinApplication
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    private var _koinApplication: KoinApplication? = null
    private val koinApplication get() = _koinApplication!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _koinApplication = initKoin(
            additionalModules = listOf(
                module {
                    single<Context> { applicationContext }
                    single<Activity> { this@MainActivity }
                }
            )
        )

        setContent {
            val qrScannerStateViewModel by viewModels<AndroidQrScannerStateViewModel>()
            val qrScannerState by qrScannerStateViewModel.qrScannerState.collectAsState()
            ScreenSelector(
                koinApplication,
                qrScannerStateViewModel,
                qrScannerState,
                this::moveToContent
            )
        }
    }

    private fun moveToContent(content: String) {
        if (content != "") {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(content)))
        }
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun ScreenSelector(
    koinApplication: KoinApplication,
    qrScannerStateViewModel: AndroidQrScannerStateViewModel,
    qrScannerState: QrScannerState,
    moveToContent: (String) -> Unit
) {
    if (qrScannerState.openQrScanner) {
        QrScanScreen(
            qrScannerStateViewModel.viewModel,
            moveToContent
        )
    } else {
        MainView(
            koin = koinApplication.koin,
            qrScannerStateViewModel = qrScannerStateViewModel.viewModel
        )
    }
}
