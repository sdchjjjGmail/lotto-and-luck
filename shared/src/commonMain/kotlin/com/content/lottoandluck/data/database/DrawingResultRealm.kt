package com.content.lottoandluck.data.database

import com.content.lottoandluck.data.model.DrawingResultEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DrawingResultRealm {
    private val schema = setOf(DrawingResultEntity::class)

    private val realm by lazy {
        val config = RealmConfiguration.Builder(schema = schema)
            .name("drawing_results")
            .log(LogLevel.ALL)
            .schemaVersion(1)
            .build()
        Realm.open(configuration = config)
    }

    suspend fun insertDrawingResult(drawingResult: DrawingResultEntity): Boolean {
        return try {
            realm.write {
                copyToRealm(drawingResult, UpdatePolicy.ALL)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getDrawingResults(): Flow<List<DrawingResultEntity>> {
        return realm.query(clazz = DrawingResultEntity::class).asFlow().map {
            when (it) {
                is InitialResults -> it.list
                is UpdatedResults -> it.list
            }
        }
    }

    fun getLastRound(): String {
        return with(realm.query<DrawingResultEntity>().find().toList()) {
            if (isEmpty()) {
                "0"
            } else {
                last().drwNo.toString()
            }
        }
    }
}