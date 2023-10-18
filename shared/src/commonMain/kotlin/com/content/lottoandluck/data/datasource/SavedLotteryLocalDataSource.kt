package com.content.lottoandluck.data.datasource

import com.content.lottoandluck.data.database.SavedLotteryRealm
import com.content.lottoandluck.data.model.mapper.mapEntityToSavedLotteryNumber
import com.content.lottoandluck.data.model.mapper.mapSavedLotteryNumberToEntity
import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.domain.model.SavedLotteryNumber
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SavedLotteryLocalDataSource(
    private val savedLotteryRealm: SavedLotteryRealm
) : SavedLotteryDataSource {
    override suspend fun saveLottery(lotteryNumber: LotteryNumber): Boolean {
        return savedLotteryRealm.saveLottery(mapSavedLotteryNumberToEntity(lotteryNumber))
    }

    override suspend fun getSavedLottery(): Flow<List<SavedLotteryNumber>> {
        return savedLotteryRealm.getSavedLotteries().map {
            mapEntityToSavedLotteryNumber(it)
        }
    }

    override suspend fun deleteSavedLottery(id: String): Boolean {
        return savedLotteryRealm.deleteSavedLottery(id)
    }

    override suspend fun deleteSavedLotteries(): Boolean {
        return savedLotteryRealm.deleteSavedLotteries()
    }
}