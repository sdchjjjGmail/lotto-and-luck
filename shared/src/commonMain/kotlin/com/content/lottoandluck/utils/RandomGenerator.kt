package com.content.lottoandluck.utils

object RandomGenerator {
    fun generate(min: Int, max: Int): Int {
        return (min..max).random()
    }
}