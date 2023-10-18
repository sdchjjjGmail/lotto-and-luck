package com.content.lottoandluck.domain.model

data class DrawingResult(
    val bnusNo: Int = 0,
    val drwNo: Int = 0,
    val drwNoDate: String = "1993.02.03",
    val drwtNo1: Int = 0,
    val drwtNo2: Int = 0,
    val drwtNo3: Int = 0,
    val drwtNo4: Int = 0,
    val drwtNo5: Int = 0,
    val drwtNo6: Int = 0,
    val firstAccumamnt: Long = 0,
    val firstPrzwnerCo: Int = 0,
    val firstWinamnt: Long = 0,
    val returnValue: String = "fail",
    val totSellamnt: Long = 0
)
