package com.content.lottoandluck.domain.repository

import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.domain.model.SavedLotteryNumber
import kotlinx.coroutines.flow.Flow

interface SavedLotteryRepository {
    suspend fun saveLottery(lotteryNumber: LotteryNumber): Boolean
    suspend fun getSavedLottery(): Flow<List<SavedLotteryNumber>>
    suspend fun deleteSavedLottery(id: String): Boolean
    suspend fun deleteSavedLotteries(): Boolean
}