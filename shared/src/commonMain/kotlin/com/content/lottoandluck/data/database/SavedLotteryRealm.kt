package com.content.lottoandluck.data.database

import com.content.lottoandluck.data.model.SavedLotteryNumberEntity
import io.github.aakira.napier.Napier
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SavedLotteryRealm {
    private val schema = setOf(SavedLotteryNumberEntity::class)

    private val realm by lazy {
        val config = RealmConfiguration.Builder(schema = schema)
            .name("saved_numbers")
            .log(LogLevel.ALL)
            .schemaVersion(1)
            .build()
        Realm.open(configuration = config)
    }

    suspend fun saveLottery(lottery: SavedLotteryNumberEntity): Boolean {
        return try {
            realm.write {
                copyToRealm(lottery, UpdatePolicy.ALL)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getSavedLotteries(): Flow<List<SavedLotteryNumberEntity>> {
        return realm.query(clazz = SavedLotteryNumberEntity::class).asFlow().map {
            when (it) {
                is InitialResults -> it.list
                is UpdatedResults -> it.list
            }
        }
    }

    suspend fun deleteSavedLottery(selectedId: String): Boolean {
        return try {
            realm.write {
                query<SavedLotteryNumberEntity>().find().first {
                    it.id.toString() == selectedId
                }.also {
                    delete(it)
                }
            }
            true
        } catch (e: Exception) {
            Napier.d("failed delete : ${e.message}")
            false
        }
    }

    suspend fun deleteSavedLotteries(): Boolean {
        return try {
            realm.write {
                deleteAll()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}