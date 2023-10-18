package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.repository.SavedLotteryRepository

class DeleteSavedLotteryUseCase(
    private val savedLotteryRepository: SavedLotteryRepository
) {
    suspend operator fun invoke(id: String): Boolean = savedLotteryRepository.deleteSavedLottery(id)
}