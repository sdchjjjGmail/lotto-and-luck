package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.repository.DrawingResultRepository
import kotlinx.coroutines.flow.Flow

class GetCachedDrawingResultUseCase(
    private val drawingResultRepository: DrawingResultRepository
) {
    suspend operator fun invoke(): Flow<Map<Int, Drawing>> =
        drawingResultRepository.getCachedDrawing()
}