package com.content.lottoandluck.data.apiservice

import com.content.lottoandluck.data.model.DrawResultResponse
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class ApiService(
    private val client: HttpClient
) {
    private val DRAW_RESULT_BASE_URL = "https://www.dhlottery.co.kr/common.do"

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
        useAlternativeNames = true
    }

    suspend fun getDrawResult(
        round: Int
    ): DrawResultResponse {
        return json.decodeFromString<DrawResultResponse>(client.get(DRAW_RESULT_BASE_URL) {
            parameter("method", "getLottoNumber")
            parameter("drwNo", round.toString())
        }.bodyAsText())
            .also {
                Napier.v("result $it")
            }
    }
}
