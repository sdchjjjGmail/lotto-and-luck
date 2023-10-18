package com.content.lottoandluck.data.repository

import com.content.lottoandluck.data.datasource.SavedLotteryLocalDataSource
import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.domain.model.SavedLotteryNumber
import com.content.lottoandluck.domain.repository.SavedLotteryRepository
import kotlinx.coroutines.flow.Flow

class SavedLotteryRepositoryImpl(
    private val savedLotteryLocalDataSource: SavedLotteryLocalDataSource
) : SavedLotteryRepository {

    override suspend fun saveLottery(lotteryNumber: LotteryNumber): Boolean {
        return savedLotteryLocalDataSource.saveLottery(lotteryNumber)
    }

    override suspend fun getSavedLottery(): Flow<List<SavedLotteryNumber>> {
        return savedLotteryLocalDataSource.getSavedLottery()
    }

    override suspend fun deleteSavedLottery(id: String): Boolean {
        return savedLotteryLocalDataSource.deleteSavedLottery(id)
    }

    override suspend fun deleteSavedLotteries(): Boolean {
        return savedLotteryLocalDataSource.deleteSavedLotteries()
    }
}