package com.content.lottoandluck.utils

import kotlinx.datetime.Clock

object LatestRound {
    private const val FIRST_TIME = 1_039_260_900_000
    private const val SEVEN_DAYS = 604_800_000

    fun get(): String {
        return (((Clock.System.now()
            .toEpochMilliseconds() - FIRST_TIME) / SEVEN_DAYS) + 1).toString()
    }
}