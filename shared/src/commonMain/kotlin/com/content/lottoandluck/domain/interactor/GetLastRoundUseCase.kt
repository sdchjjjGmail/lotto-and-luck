package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.repository.DrawingResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLastRoundUseCase(
    private val drawingResultRepository: DrawingResultRepository
) {
    suspend operator fun invoke(): Flow<String> = flow {
        emit(
            drawingResultRepository.getLatestRound()
        )
    }
}