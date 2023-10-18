package com.content.lottoandluck.domain.repository

import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.DrawingResult
import kotlinx.coroutines.flow.Flow

interface DrawingResultRepository {
    suspend fun getCachedDrawing(): Flow<Map<Int, Drawing>>
    suspend fun getDrawing(round: Int): DrawingResult
    suspend fun getLatestRound(): String
    suspend fun putDrawingResult(drawingResult: DrawingResult): Boolean
}