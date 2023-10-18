package com.content.lottoandluck.utils

import com.content.lottoandluck.MR
import dev.icerock.moko.resources.ColorResource

object ColorUtil {
    fun getColorCodeByNumber(number: Int): ColorResource {
        if (number == 0) return MR.colors.white
        when (number / 10) {
            0 -> return MR.colors.ones
            1 -> return MR.colors.tens
            2 -> return MR.colors.twenties
            3 -> return MR.colors.thirties
            4 -> return MR.colors.forties
        }
        return MR.colors.white
    }
}