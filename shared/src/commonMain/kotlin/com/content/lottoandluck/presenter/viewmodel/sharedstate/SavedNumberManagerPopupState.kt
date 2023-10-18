package com.content.lottoandluck.presenter.viewmodel.sharedstate

import androidx.compose.runtime.mutableStateOf

object SavedNumberManagerPopupState {
    val showPopup = mutableStateOf(false)
    val popupAct = mutableStateOf("")
    val animation = mutableStateOf(true)

    fun saveAskPopup() {
        popupAct.value = "저장"
        showPopup()
    }

    fun deleteAskPopup(type: String) {
        popupAct.value = type
        showPopup()
    }

    private fun showPopup() {
        stopAnimation()
        showPopup.value = true
    }

    fun hidePopup() {
        showPopup.value = false
        startAnimation()
    }

    private fun startAnimation() {
        animation.value = true
    }

    private fun stopAnimation() {
        animation.value = false

    }
}