package com.content.lottoandluck.domain.model

data class Drawing(
    val round: Int = -1,
    val date: String = "",
    val numberList: ArrayList<Int> = ArrayList(),
    val bonusNumber: Int = 0,
)
