package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.model.DrawingResult
import com.content.lottoandluck.domain.repository.DrawingResultRepository


class PutDrawingResultUseCase(
    private val drawingResultRepository: DrawingResultRepository
) {
    suspend operator fun invoke(drawingResult: DrawingResult): Boolean {
        return drawingResultRepository.putDrawingResult(drawingResult)
    }
}