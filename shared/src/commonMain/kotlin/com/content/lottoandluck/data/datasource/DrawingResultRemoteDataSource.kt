package com.content.lottoandluck.data.datasource

import com.content.lottoandluck.domain.model.DrawingResult

interface DrawingResultRemoteDataSource {
    suspend fun getDrawing(round: Int): DrawingResult
}