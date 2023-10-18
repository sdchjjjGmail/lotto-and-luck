package com.content.lottoandluck.presenter.viewmodel.sharedstate

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ToastMessageState {
    val show = mutableStateOf(false)

    fun show() {
        if (!show.value) show.value = true
        CoroutineScope(Dispatchers.Default).launch {
            delay(700)
            hide()
        }
    }

    fun hide() {
        show.value = false
    }
}