package com.content.lottoandluck.di

import com.content.lottoandluck.data.repository.DrawingResultRepositoryImpl
import com.content.lottoandluck.data.repository.SavedLotteryRepositoryImpl
import com.content.lottoandluck.domain.interactor.DeleteSavedLotteriesUseCase
import com.content.lottoandluck.domain.interactor.DeleteSavedLotteryUseCase
import com.content.lottoandluck.domain.interactor.GetCachedDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetLastRoundUseCase
import com.content.lottoandluck.domain.interactor.GetSavedLotteryUseCase
import com.content.lottoandluck.domain.interactor.PutDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.SaveLotteryNumberUseCase
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.direct
import org.kodein.di.instance

class UseCaseDi {
    private val drawingResultRepository: DrawingResultRepositoryImpl by RepositoryFactory.repository.instance()
    private val savedLotteryRepository: SavedLotteryRepositoryImpl by RepositoryFactory.repository.instance()

    private val useCaseModule = DI.Module("usecase") {
        bindSingleton { GetDrawingResultUseCase(drawingResultRepository) }
        bindSingleton { GetLastRoundUseCase(drawingResultRepository) }
        bindSingleton { SaveLotteryNumberUseCase(savedLotteryRepository) }
        bindSingleton { GetSavedLotteryUseCase(savedLotteryRepository) }
        bindSingleton { PutDrawingResultUseCase(drawingResultRepository) }
        bindSingleton { GetCachedDrawingResultUseCase(drawingResultRepository) }
        bindSingleton { DeleteSavedLotteryUseCase(savedLotteryRepository) }
        bindSingleton { DeleteSavedLotteriesUseCase(savedLotteryRepository) }
    }
    val useCaseDi = DI {
        import(useCaseModule)
    }
}

object UseCaseFactory {
    val useCase = UseCaseDi().useCaseDi

    val diDirect = useCase.direct

    inline fun <reified T : Any> resolve(): T {
        return diDirect.instance()
    }
}

interface UseCaseConfigurator {
    fun createSampleUseCase(): GetDrawingResultUseCase?
    fun createSaveLotteryNumberUseCase(): SaveLotteryNumberUseCase
    fun createGetSavedLotteryUseCase(): GetSavedLotteryUseCase
}

class UseCaseConfiguratorImpl : UseCaseConfigurator {
    override fun createSampleUseCase(): GetDrawingResultUseCase {
        return UseCaseFactory.resolve()
    }

    override fun createSaveLotteryNumberUseCase(): SaveLotteryNumberUseCase {
        return UseCaseFactory.resolve()
    }

    override fun createGetSavedLotteryUseCase(): GetSavedLotteryUseCase {
        return UseCaseFactory.resolve()
    }
}

object UseCaseConfigFactory {
    fun createSampleUseCase(): GetDrawingResultUseCase {
        return UseCaseConfiguratorImpl().createSampleUseCase()
    }

    fun createSavedLotteryNumberUseCase(): SaveLotteryNumberUseCase {
        return UseCaseConfiguratorImpl().createSaveLotteryNumberUseCase()
    }

    fun createGetSavedLotteryUseCase(): GetSavedLotteryUseCase {
        return UseCaseConfiguratorImpl().createGetSavedLotteryUseCase()
    }
}