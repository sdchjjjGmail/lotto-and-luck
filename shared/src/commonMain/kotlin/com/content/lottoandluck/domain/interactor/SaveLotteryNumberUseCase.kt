package com.content.lottoandluck.domain.interactor

import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.domain.repository.SavedLotteryRepository

class SaveLotteryNumberUseCase(
    private val savedLotteryRepository: SavedLotteryRepository
) {
    suspend operator fun invoke(lotteryNumber: LotteryNumber): Boolean =
        savedLotteryRepository.saveLottery(lotteryNumber)
}