package com.content.lottoandluck.data.model.mapper

import com.content.lottoandluck.data.model.SavedLotteryNumberEntity
import com.content.lottoandluck.domain.model.LotteryNumber
import com.content.lottoandluck.domain.model.SavedLotteryNumber

fun mapSavedLotteryNumberToEntity(savedLotteryNumber: LotteryNumber): SavedLotteryNumberEntity {
    return with(SavedLotteryNumberEntity()) {
        for (singleNumber: Int in savedLotteryNumber.numberList) {
            numbers.add(singleNumber)
        }
        this@with
    }
}

fun mapEntityToSavedLotteryNumber(entityList: List<SavedLotteryNumberEntity>): List<SavedLotteryNumber> {
    return with(ArrayList<SavedLotteryNumber>()) {
        for (singleData: SavedLotteryNumberEntity in entityList) {
            add(
                SavedLotteryNumber(
                    id = singleData.id.toString(),
                    numberList = singleData.numbers
                )
            )
        }
        this@with
    }
}