package com.content.lottoandluck.data.datasource

import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.DrawingResult
import kotlinx.coroutines.flow.Flow

interface DrawingResultLocalDataSource {
    suspend fun getCachedDrawing(): Flow<Map<Int, Drawing>>
    suspend fun getLatestRound(): String
    suspend fun putDrawingResult(drawingResult: DrawingResult): Boolean
}