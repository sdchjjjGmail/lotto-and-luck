package com.content.lottoandluck.presenter.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.content.lottoandluck.domain.interactor.GetDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetLastRoundUseCase
import com.content.lottoandluck.domain.interactor.PutDrawingResultUseCase
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.utils.LatestRound
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DownloadViewModel(
    private val getDrawingResultUseCase: GetDrawingResultUseCase,
    private val getLastRoundUseCase: GetLastRoundUseCase,
    private val putDrawingResultUseCase: PutDrawingResultUseCase
) {
    val showProgress = mutableStateOf(false)
    val percentage = mutableStateOf("")
    val progress = mutableStateOf(0f)

    val loading = mutableStateOf(false)

    private var keep = true
    private var downloadingRound = 1
    private var lastRound = "1"

    init {
        init()
    }

    private fun init() {
        lastRound = LatestRound.get()
        getLatestSavedRound()
    }

    fun ok() {
        DrawingDownloadState.hideDownloadPopup()
        getDrawingResult()
    }

    fun cancel() {
        DrawingDownloadState.hideDownloadPopup()
        DrawingDownloadState.closeDownloadScreen()
    }

    private fun getLatestSavedRound() {
        lastRound = LatestRound.get()
        CoroutineScope(Dispatchers.Default).launch {
            getLastRoundUseCase.invoke()
                .onStart {
                    runBlocking(Dispatchers.Main) {
                        loading.value = true
                    }
                }
                .onCompletion {
                    runBlocking(Dispatchers.Main) {
                        loading.value = false
                    }
                }
                .collect {
                    if (it == lastRound) {
                        DrawingDownloadState.ready()
                        DrawingDownloadState.closeDownloadScreen()
                    } else {
                        downloadingRound = it.toInt() + 1
                        DrawingDownloadState.showDownloadPopup(it.toInt())
                    }
                    DrawingDownloadState.setLastRound(it.toInt())
                }
        }
    }

    private fun getDrawingResult() {
        showProgress.value = true
        CoroutineScope(Dispatchers.Default).launch {
            do {
                getDrawingResultUseCase(downloadingRound)
                    .catch {
                        runBlocking(Dispatchers.Main) {
                            showProgress.value = false
                        }
                        it.printStackTrace()
                    }
                    .collect {
                        if (it.returnValue == "success") {
                            putDrawingResultUseCase(it)
                            updateProgress()
                            DrawingDownloadState.setLastRound(downloadingRound)
                            downloadingRound++
                        } else {
                            keep = false
                            DrawingDownloadState.ready()
                            DrawingDownloadState.closeDownloadScreen()
                            runBlocking(Dispatchers.Main) {
                                showProgress.value = false
                            }
                        }
                    }
            } while (keep)
        }
    }

    private fun updateProgress() {
        runBlocking(Dispatchers.Main) {
//            progress.value = downloadingRound / lastRound.toFloat()
            progress.value = (downloadingRound / lastRound.toFloat()) * 100
            percentage.value =
                "${((downloadingRound / lastRound.toFloat()) * 100).toInt()}%"
        }
    }
}