package com.content.lottoandluck.data.datasource

import com.content.lottoandluck.data.apiservice.ApiService
import com.content.lottoandluck.data.model.mapper.toDomain
import com.content.lottoandluck.domain.model.DrawingResult

class DrawingResultRemoteDataSourceImpl(
    private val apiService: ApiService
) : DrawingResultRemoteDataSource {
    override suspend fun getDrawing(round: Int): DrawingResult {
        return apiService.getDrawResult(round).toDomain()
    }
}