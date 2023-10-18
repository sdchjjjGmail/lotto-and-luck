package com.content.lottoandluck.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DrawResultResponse(
    @SerialName("bnusNo")
    val bnusNo: Int = 0,
    @SerialName("drwNo")
    val drwNo: Int = 0,
    @SerialName("drwNoDate")
    val drwNoDate: String = "",
    @SerialName("drwtNo1")
    val drwtNo1: Int = 0,
    @SerialName("drwtNo2")
    val drwtNo2: Int = 0,
    @SerialName("drwtNo3")
    val drwtNo3: Int = 0,
    @SerialName("drwtNo4")
    val drwtNo4: Int = 0,
    @SerialName("drwtNo5")
    val drwtNo5: Int = 0,
    @SerialName("drwtNo6")
    val drwtNo6: Int = 0,
    @SerialName("firstAccumamnt")
    val firstAccumamnt: Long = 0,
    @SerialName("firstPrzwnerCo")
    val firstPrzwnerCo: Int = 0,
    @SerialName("firstWinamnt")
    val firstWinamnt: Long = 0,
    @SerialName("returnValue")
    val returnValue: String = "fail",
    @SerialName("totSellamnt")
    val totSellamnt: Long = 0
)