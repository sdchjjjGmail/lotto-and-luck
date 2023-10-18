package com.content.lottoandluck.data.repository

import com.content.lottoandluck.data.datasource.DrawingResultLocalDataSource
import com.content.lottoandluck.data.datasource.DrawingResultRemoteDataSource
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.DrawingResult
import com.content.lottoandluck.domain.repository.DrawingResultRepository
import kotlinx.coroutines.flow.Flow

class DrawingResultRepositoryImpl(
    private val drawingResultLocalDataSource: DrawingResultLocalDataSource,
    private val drawingResultRemoteDataSource: DrawingResultRemoteDataSource
) : DrawingResultRepository {
    override suspend fun getCachedDrawing(): Flow<Map<Int, Drawing>> {
        return drawingResultLocalDataSource.getCachedDrawing()
    }

    override suspend fun getDrawing(round: Int): DrawingResult {
        return drawingResultRemoteDataSource.getDrawing(round)
    }

    override suspend fun getLatestRound(): String {
        return drawingResultLocalDataSource.getLatestRound()
    }

    override suspend fun putDrawingResult(drawingResult: DrawingResult): Boolean {
        return drawingResultLocalDataSource.putDrawingResult(drawingResult)
    }
}