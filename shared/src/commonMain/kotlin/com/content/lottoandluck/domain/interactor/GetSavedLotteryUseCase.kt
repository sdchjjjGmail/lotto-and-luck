package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.model.SavedLotteryNumber
import com.content.lottoandluck.domain.repository.SavedLotteryRepository
import kotlinx.coroutines.flow.Flow

class GetSavedLotteryUseCase(
    private val savedLotteryRepository: SavedLotteryRepository
) {
    suspend operator fun invoke(): Flow<List<SavedLotteryNumber>> =
        savedLotteryRepository.getSavedLottery()
}