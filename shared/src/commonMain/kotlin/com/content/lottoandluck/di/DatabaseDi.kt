package com.content.lottoandluck.di

import com.content.lottoandluck.data.database.DrawingResultRealm
import com.content.lottoandluck.data.database.SavedLotteryRealm
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.direct
import org.kodein.di.instance

class DatabaseDi {
    private val realmModule = DI.Module("realm") {
        bindSingleton { DrawingResultRealm() }
        bindSingleton { SavedLotteryRealm() }
    }
    val databaseDi = DI {
        import(realmModule)
    }
}

object DatabaseFactory {
    val database = DatabaseDi().databaseDi

    val diDirect = database.direct

    inline fun <reified T : Any> resolve(): T {
        return diDirect.instance()
    }
}

interface DatabaseConfigurator {
    fun createSavedLotteryNumberRealm(): SavedLotteryRealm?
}

class DatabaseConfiguratorImpl : DatabaseConfigurator {
    override fun createSavedLotteryNumberRealm(): SavedLotteryRealm {
        return DatabaseFactory.resolve()
    }
}

object DatabaseConfigFactory {
    fun createSavedLotteryNumberRealm(): SavedLotteryRealm {
        return DatabaseConfiguratorImpl().createSavedLotteryNumberRealm()
    }
}