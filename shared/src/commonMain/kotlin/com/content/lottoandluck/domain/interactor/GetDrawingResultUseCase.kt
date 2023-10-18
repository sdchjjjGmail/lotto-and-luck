package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.model.DrawingResult
import com.content.lottoandluck.domain.repository.DrawingResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDrawingResultUseCase(
    private val drawingResultRepository: DrawingResultRepository
) {
    suspend operator fun invoke(round: Int): Flow<DrawingResult> = flow {
        emit(
            drawingResultRepository.getDrawing(round)
        )
    }
}