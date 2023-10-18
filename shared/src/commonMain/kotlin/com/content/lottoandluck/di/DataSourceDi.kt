package com.content.lottoandluck.di

import com.content.lottoandluck.data.apiservice.ApiService
import com.content.lottoandluck.data.database.DrawingResultRealm
import com.content.lottoandluck.data.database.SavedLotteryRealm
import com.content.lottoandluck.data.datasource.DrawingResultLocalDataSourceImpl
import com.content.lottoandluck.data.datasource.DrawingResultRemoteDataSourceImpl
import com.content.lottoandluck.data.datasource.SavedLotteryLocalDataSource
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

class DataSourceDi {
    private val apiService: ApiService by ApiServiceFactory.apiService.instance()
    private val drawingResultRealm: DrawingResultRealm by DatabaseFactory.database.instance()
    private val savedLotteryRealm: SavedLotteryRealm by DatabaseFactory.database.instance()

    private val dataSourceModule = DI.Module("datasource") {
        bindSingleton { DrawingResultLocalDataSourceImpl(drawingResultRealm) }
        bindSingleton { DrawingResultRemoteDataSourceImpl(apiService) }
        bindSingleton { SavedLotteryLocalDataSource(savedLotteryRealm) }
    }
    val dataSourceDi = DI {
        import(dataSourceModule)
    }
}

object DataSourceFactory {
    val dataSource = DataSourceDi().dataSourceDi
}
