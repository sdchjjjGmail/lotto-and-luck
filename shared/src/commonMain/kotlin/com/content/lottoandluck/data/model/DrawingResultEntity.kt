package com.content.lottoandluck.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class DrawingResultEntity : RealmObject {
    @PrimaryKey
    var id: RealmUUID = RealmUUID.random()
    var bnusNo: Int = 0
    var drwNo: Int = 0
    var drwNoDate: String = "1993.02.03"
    var drwtNo1: Int = 0
    var drwtNo2: Int = 0
    var drwtNo3: Int = 0
    var drwtNo4: Int = 0
    var drwtNo5: Int = 0
    var drwtNo6: Int = 0
    var firstAccumamnt: Long = 0
    var firstPrzwnerCo: Int = 0
    var firstWinamnt: Long = 0
    var returnValue: String = "fail"
    var totSellamnt: Long = 0
}