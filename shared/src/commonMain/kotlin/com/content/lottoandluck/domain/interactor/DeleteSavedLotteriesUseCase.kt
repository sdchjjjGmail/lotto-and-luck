package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.repository.SavedLotteryRepository

class DeleteSavedLotteriesUseCase(
    private val savedLotteryRepository: SavedLotteryRepository
) {
    suspend operator fun invoke(): Boolean = savedLotteryRepository.deleteSavedLotteries()
}