package com.content.lottoandluck.utils

object DropDownDrawingRoundCalculator {
    fun getList(selectedRound: Int, latestRound: Int): List<Int> {
        with(ArrayList<Int>()) {
            for (round in (selectedRound + 2) downTo (selectedRound - 2)) {
                when (round) {
                    -3 -> add(latestRound - 3)
                    -2 -> add(latestRound - 2)
                    -1 -> add(latestRound - 1)
                    0 -> add(latestRound)
                    (latestRound + 1) -> add(1)
                    (latestRound + 2) -> add(2)
                    else -> add(round)
                }
            }
            return this@with
        }
    }
}