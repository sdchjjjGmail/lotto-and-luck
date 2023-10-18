package com.content.lottoandluck.utils

import com.content.lottoandluck.domain.model.Drawing
import kotlin.math.round


object NumberAnalyzer {
    fun getTotalCount(number: Int, drawingMap: HashMap<Int, Drawing>): Int {
        return with(drawingMap) {
            var count = 0
            forEach { (key) ->
                if (get(key)!!.numberList.contains(number)) {
                    count++
                }
            }
            count
        }
    }

    fun getBonusCount(number: Int, drawingMap: HashMap<Int, Drawing>): Int {
        return with(drawingMap) {
            var count = 0
            forEach { (key) ->
                if (get(key)!!.bonusNumber == number) {
                    count++
                }
            }
            count
        }
    }

    fun getDrawingPattern(
        number: Int,
        drawingMap: HashMap<Int, Drawing>,
        typeBonus: Boolean
    ): HashMap<Int, Int> {
        if (typeBonus) {
            return bonusPattern(number, drawingMap)
        }
        with(HashMap<Int, Int>()) {
            var counting = false
            var skipRound = 0
            for (i in 1..drawingMap.size) {
                skipRound++
                if (drawingMap[i]!!.numberList.contains(number)) {
                    if (!counting) {
                        this@with[skipRound] = getMapValueOrDefault(this@with, skipRound).plus(1)
                        counting = true
                    } else {
                        this@with[skipRound] = getMapValueOrDefault(this@with, skipRound).plus(1)
                    }
                    skipRound = 0
                }
            }
            return this@with
        }
    }

    private fun bonusPattern(
        number: Int,
        drawingMap: HashMap<Int, Drawing>
    ): HashMap<Int, Int> {
        with(HashMap<Int, Int>()) {
            var counting = false
            var skipRound = 0
            for (i in 1..drawingMap.size) {
                skipRound++
                if (drawingMap[i]!!.bonusNumber == number) {
                    if (!counting) {
                        this@with[skipRound] = getMapValueOrDefault(this@with, skipRound).plus(1)
                        counting = true
                    } else {
                        this@with[skipRound] = getMapValueOrDefault(this@with, skipRound).plus(1)
                    }
                    skipRound = 0
                }
            }
            return this@with
        }
    }

    private fun getMapValueOrDefault(map: HashMap<Int, Int>, key: Int): Int {
        return if (map[key] == null) {
            0
        } else {
            map[key]!!
        }
    }

    fun getPopPercentage(count: Double, totalCount: Double): String {
        return (round(((count / totalCount) * 100) * 100) / 100).toString()
    }

    fun getLastPop(number: Int, drawingMap: HashMap<Int, Drawing>): Int {
        var lastRound = 0
        for (i in 1..drawingMap.size) {
            if (drawingMap[i]!!.numberList.contains(number)) {
                lastRound = i
            }
        }
        return drawingMap.size - lastRound
    }

//    fun getLastBonusPop(number: Int, drawingMap: HashMap<Int, Drawing>): Int {
//        var lastRound = 0
//        for (i in 1..drawingMap.size) {
//            if (drawingMap[i]!!.bonusNumber == number) {
//                lastRound = i
//            }
//        }
//        return drawingMap.size - lastRound
//    }

    fun getNumberPlaceDuplicateCount(drawing: ArrayList<Int>): HashMap<Int, Int> {
        with(HashMap<Int, Int>()) {
            drawing.forEach {
                when (it / 10) {
                    0 -> set(1, getMapValueOrDefault(this@with, 1).plus(1))
                    1 -> set(10, getMapValueOrDefault(this@with, 10).plus(1))
                    2 -> set(20, getMapValueOrDefault(this@with, 20).plus(1))
                    3 -> set(30, getMapValueOrDefault(this@with, 30).plus(1))
                    4 -> set(40, getMapValueOrDefault(this@with, 40).plus(1))
                }
            }
            return this@with
        }
    }

    fun getNumberPlacePercentage(count: Double, totalCount: Double): String {
        return ((round((count / totalCount) * 100) * 100) / 100).toString()
    }

    fun getSerialDrawingCountMap(drawingMap: HashMap<Int, Drawing>): HashMap<Int, Int> {
        with(HashMap<Int, Int>()) {
            drawingMap.forEach { (key, value) ->
                val serialNumberCount: Int
                if (drawingMap[key + 1] != null) {
                    serialNumberCount =
                        getSerialCount(value.numberList, drawingMap[key + 1]!!.numberList)
                    this@with[serialNumberCount] =
                        getMapValueOrDefault(this@with, serialNumberCount).plus(1)
                }
            }
            return this@with
        }
    }

    fun getSerialCount(
        currentDrawingList: ArrayList<Int>,
        compareDrawingList: ArrayList<Int>
    ): Int {
        var serialNumberCount = 0
        currentDrawingList.forEach {
            if (compareDrawingList.contains(it)) {
                serialNumberCount++
            }
        }
        return serialNumberCount
    }

    fun getSerialCountPercentage(count: Double, totalCount: Double): String {
        return (round(((count / totalCount) * 100) * 100) / 100).toString()
    }

    fun getMultipleSerialPopOfSingleNumberCount(drawingMap: HashMap<Int, Drawing>): HashMap<Int, HashMap<Int, Int>> {
        with(HashMap<Int, HashMap<Int, Int>>()) {
            for (i in 1..45) {
                set(i, getSingleNumberSerialPopCountMap(drawingMap, i))
            }
            return this@with
        }
    }

    private fun getSingleNumberSerialPopCountMap(
        drawingMap: HashMap<Int, Drawing>,
        num: Int
    ): HashMap<Int, Int> {
        var firstPop = true
        var serialCount = 0
        with(HashMap<Int, Int>()) {
            drawingMap.forEach { (key, value) ->
                if (value.numberList.contains(num)) {
                    if (!firstPop) {
                        serialCount++
                    } else {
                        firstPop = false
                        serialCount++
                    }
                } else {
                    if (serialCount != 0) {
                        set(serialCount, getMapValueOrDefault(this@with, serialCount).plus(1))
                        serialCount = 0
                    }
                }
            }
            if (serialCount != 0) {
                set(serialCount, getMapValueOrDefault(this@with, serialCount).plus(1))
                serialCount = 0
            }
            return this@with
        }
    }

    fun getSerialPop(num: Int, drawingMap: HashMap<Int, Drawing>, lastRound: Int): Int {
        var serialCount = 0
        var round = lastRound
        for (i in 1..lastRound) {
            if (drawingMap[round]!!.numberList.contains(num)) {
                serialCount++
                round--
            } else {
                return serialCount
            }
        }
        return serialCount
    }

    fun getSerialPopPercentage(count: Double, totalCount: Double): String {
        return (round(((count / totalCount) * 100) * 100) / 100).toString()
    }
}