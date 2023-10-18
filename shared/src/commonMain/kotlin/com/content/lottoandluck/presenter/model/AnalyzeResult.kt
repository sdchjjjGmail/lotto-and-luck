package com.content.lottoandluck.presenter.model

data class AnalyzeResult(
    val skipRound: Int = 0,
    val skipRoundText: String = "",
    val skipRoundCount: String = "",
    val popPercentage: String = "",
    val lastPop: Int = 0,
)
