package com.content.lottoandluck.di

import com.content.lottoandluck.domain.interactor.DeleteSavedLotteriesUseCase
import com.content.lottoandluck.domain.interactor.DeleteSavedLotteryUseCase
import com.content.lottoandluck.domain.interactor.GetCachedDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.GetLastRoundUseCase
import com.content.lottoandluck.domain.interactor.GetSavedLotteryUseCase
import com.content.lottoandluck.domain.interactor.PutDrawingResultUseCase
import com.content.lottoandluck.domain.interactor.SaveLotteryNumberUseCase
import com.content.lottoandluck.presenter.viewmodel.DownloadViewModel
import com.content.lottoandluck.presenter.viewmodel.MainViewModel
import com.content.lottoandluck.presenter.viewmodel.RandomViewModel
import com.content.lottoandluck.presenter.viewmodel.RecommendationViewModel
import com.content.lottoandluck.presenter.viewmodel.SaveViewModel
import com.content.lottoandluck.presenter.viewmodel.SearchViewModel
import com.content.lottoandluck.presenter.viewmodel.StatisticsViewModel
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

class ViewModelDi {
    private val getDrawingResultUseCase: GetDrawingResultUseCase by UseCaseFactory.useCase.instance()
    private val getLastRoundUseCase: GetLastRoundUseCase by UseCaseFactory.useCase.instance()
    private val saveLotteryNumberUseCase: SaveLotteryNumberUseCase by UseCaseFactory.useCase.instance()
    private val getSavedLotteryUseCase: GetSavedLotteryUseCase by UseCaseFactory.useCase.instance()
    private val putDrawingResultUseCase: PutDrawingResultUseCase by UseCaseFactory.useCase.instance()
    private val getCachedDrawingResultUseCase: GetCachedDrawingResultUseCase by UseCaseFactory.useCase.instance()
    private val deleteSavedLotteryUseCase: DeleteSavedLotteryUseCase by UseCaseFactory.useCase.instance()
    private val deleteSavedLotteriesUseCase: DeleteSavedLotteriesUseCase by UseCaseFactory.useCase.instance()

    private val viewModelModule = DI.Module("viewModel") {
        bindSingleton {
            MainViewModel()
        }
        bindSingleton {
            DownloadViewModel(
                getDrawingResultUseCase,
                getLastRoundUseCase,
                putDrawingResultUseCase
            )
        }
        bindSingleton {
            RandomViewModel(saveLotteryNumberUseCase)
        }
        bindSingleton {
            RecommendationViewModel(
                getLastRoundUseCase,
                getCachedDrawingResultUseCase,
                saveLotteryNumberUseCase
            )
        }
        bindSingleton {
            SaveViewModel(
                getSavedLotteryUseCase,
                deleteSavedLotteryUseCase,
                deleteSavedLotteriesUseCase,
            )
        }
        bindSingleton {
            SearchViewModel(
                getCachedDrawingResultUseCase,
                getDrawingResultUseCase
            )
        }
        bindSingleton {
            StatisticsViewModel(getCachedDrawingResultUseCase)
        }
    }
    val viewModelDi = DI {
        import(viewModelModule)
    }
}

object ViewModelFactory {
    val viewModel = ViewModelDi().viewModelDi
}
