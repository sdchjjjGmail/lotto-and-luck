package com.content.lottoandluck.data.datasource

import com.content.lottoandluck.data.database.DrawingResultRealm
import com.content.lottoandluck.data.model.mapper.mapDrawingResultToEntity
import com.content.lottoandluck.data.model.mapper.mapRawDataToCoreData
import com.content.lottoandluck.domain.model.Drawing
import com.content.lottoandluck.domain.model.DrawingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DrawingResultLocalDataSourceImpl(
    private val drawingResultRealm: DrawingResultRealm
) : DrawingResultLocalDataSource {
    override suspend fun getCachedDrawing(): Flow<Map<Int, Drawing>> {
        return drawingResultRealm.getDrawingResults().map { dataList ->
            dataList.map { singleData ->
                mapRawDataToCoreData(singleData)
            }
        }.map { dataList ->
            dataList.associateBy { singleData ->
                singleData.round
            }
        }
    }

    override suspend fun getLatestRound(): String {
        return drawingResultRealm.getLastRound()
    }

    override suspend fun putDrawingResult(drawingResult: DrawingResult): Boolean {
        return drawingResultRealm.insertDrawingResult(mapDrawingResultToEntity(drawingResult))
    }
}