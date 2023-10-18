package com.content.lottoandluck.di

import com.content.lottoandluck.data.datasource.DrawingResultLocalDataSourceImpl
import com.content.lottoandluck.data.datasource.DrawingResultRemoteDataSourceImpl
import com.content.lottoandluck.data.datasource.SavedLotteryLocalDataSource
import com.content.lottoandluck.data.repository.DrawingResultRepositoryImpl
import com.content.lottoandluck.data.repository.SavedLotteryRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

class RepositoryDi {
    private val drawingResultLocalDataSource: DrawingResultLocalDataSourceImpl by DataSourceFactory.dataSource.instance()
    private val drawingResultRemoteDataSource: DrawingResultRemoteDataSourceImpl by DataSourceFactory.dataSource.instance()
    private val savedLotteryDataSource: SavedLotteryLocalDataSource by DataSourceFactory.dataSource.instance()

    private val repositoryModule = DI.Module("repository") {
        bindSingleton {
            DrawingResultRepositoryImpl(
                drawingResultLocalDataSource,
                drawingResultRemoteDataSource
            )
        }
        bindSingleton { SavedLotteryRepositoryImpl(savedLotteryDataSource) }
    }
    val repositoryDi = DI {
        import(repositoryModule)
    }
}

object RepositoryFactory {
    val repository = RepositoryDi().repositoryDi
}
