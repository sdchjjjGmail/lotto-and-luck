package com.content.lottoandluck.data.model.mapper

import com.content.lottoandluck.data.model.DrawResultResponse
import com.content.lottoandluck.data.model.DrawingResultEntity
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.DrawingResult


fun DrawResultResponse.toDomain(): DrawingResult {
    return DrawingResult(
        bnusNo = this.bnusNo,
        drwNo = this.drwNo,
        drwNoDate = this.drwNoDate,
        drwtNo1 = this.drwtNo1,
        drwtNo2 = this.drwtNo2,
        drwtNo3 = this.drwtNo3,
        drwtNo4 = this.drwtNo4,
        drwtNo5 = this.drwtNo5,
        drwtNo6 = this.drwtNo6,
        firstAccumamnt = this.firstAccumamnt,
        firstPrzwnerCo = this.firstPrzwnerCo,
        firstWinamnt = this.firstWinamnt,
        returnValue = this.returnValue,
        totSellamnt = this.totSellamnt,
    )
}

fun DrawingResultEntity.toDomain(): DrawingResult {
    return DrawingResult(
        bnusNo = this.bnusNo,
        drwNo = this.drwNo,
        drwNoDate = this.drwNoDate,
        drwtNo1 = this.drwtNo1,
        drwtNo2 = this.drwtNo2,
        drwtNo3 = this.drwtNo3,
        drwtNo4 = this.drwtNo4,
        drwtNo5 = this.drwtNo5,
        drwtNo6 = this.drwtNo6,
        firstAccumamnt = this.firstAccumamnt,
        firstPrzwnerCo = this.firstPrzwnerCo,
        firstWinamnt = this.firstWinamnt,
        returnValue = this.returnValue,
        totSellamnt = this.totSellamnt,
    )
}

fun mapDrawingResultToEntity(drawingResult: DrawingResult): DrawingResultEntity {
    with(drawingResult) {
        return DrawingResultEntity().apply {
            bnusNo = this@with.bnusNo
            drwNo = this@with.drwNo
            drwNoDate = this@with.drwNoDate
            drwtNo1 = this@with.drwtNo1
            drwtNo2 = this@with.drwtNo2
            drwtNo3 = this@with.drwtNo3
            drwtNo4 = this@with.drwtNo4
            drwtNo5 = this@with.drwtNo5
            drwtNo6 = this@with.drwtNo6
            firstAccumamnt = this@with.firstAccumamnt
            firstPrzwnerCo = this@with.firstPrzwnerCo
            firstWinamnt = this@with.firstWinamnt
            returnValue = this@with.returnValue
            totSellamnt = this@with.totSellamnt
        }
    }
}

fun mapRawDataToCoreData(drawingResultEntity: DrawingResultEntity): Drawing {
    with(ArrayList<Int>()) {
        add(drawingResultEntity.drwtNo1)
        add(drawingResultEntity.drwtNo2)
        add(drawingResultEntity.drwtNo3)
        add(drawingResultEntity.drwtNo4)
        add(drawingResultEntity.drwtNo5)
        add(drawingResultEntity.drwtNo6)
        return Drawing(
            round = drawingResultEntity.drwNo,
            date = drawingResultEntity.drwNoDate,
            numberList = this@with,
            bonusNumber = drawingResultEntity.bnusNo,
        )
    }
}

fun mapRowDrawingResultToCoreData(drawingResult: DrawingResult): Drawing {
    with(ArrayList<Int>()) {
        add(drawingResult.drwtNo1)
        add(drawingResult.drwtNo2)
        add(drawingResult.drwtNo3)
        add(drawingResult.drwtNo4)
        add(drawingResult.drwtNo5)
        add(drawingResult.drwtNo6)
        return Drawing(
            round = drawingResult.drwNo,
            date = drawingResult.drwNoDate,
            numberList = this@with,
            bonusNumber = drawingResult.bnusNo,
        )
    }
}