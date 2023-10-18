import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.content.lottoandluck.MR
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel
import com.content.lottoandluck.permissions.service.PermissionsService
import com.content.lottoandluck.permissions.service.PermissionsServiceImpl
import com.content.lottoandluck.presenter.screen.DownloadScreen
import com.content.lottoandluck.presenter.screen.RandomScreen
import com.content.lottoandluck.presenter.screen.RecommendationScreen
import com.content.lottoandluck.presenter.screen.SaveScreen
import com.content.lottoandluck.presenter.screen.SearchScreen
import com.content.lottoandluck.presenter.screen.StatisticsScreen
import com.content.lottoandluck.presenter.screen.sharedcomposable.SavedToastMessage
import com.content.lottoandluck.presenter.viewmodel.MainViewModel
import com.content.lottoandluck.presenter.viewmodel.SearchViewModel
import com.content.lottoandluck.presenter.viewmodel.sharedstate.DrawingDownloadState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.SavedNumberManagerPopupState
import com.content.lottoandluck.presenter.viewmodel.sharedstate.StatisticsState
import com.content.lottoandluck.utils.Body1
import com.content.lottoandluck.utils.ScreenProvider
import dev.icerock.moko.resources.compose.colorResource
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.module.Module
import org.koin.dsl.module


@Composable
fun App(
    permissionsService: PermissionsService,
    viewModel: MainViewModel,
    searchViewModel: SearchViewModel,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(colorResource(MR.colors.white))
        )
        AppScreen(
            viewModel,
            searchViewModel,
            permissionsService,
            qrScannerStateViewModel
        )
    }
}

@Composable
fun AppScreen(
    viewModel: MainViewModel,
    searchViewModel: SearchViewModel,
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    if (DrawingDownloadState.ready.value) {
        searchViewModel.init()
    }

    AnimatedVisibility(
        visible = DrawingDownloadState.showDownloadScreen.value,
        enter = slideInVertically(),
        exit = fadeOut()
    ) {
        DownloadScreen()
    }
    AnimatedVisibility(
        visible = !DrawingDownloadState.showDownloadScreen.value,
        enter = expandVertically(expandFrom = Alignment.CenterVertically),
        exit = fadeOut()
    ) {
        Column(Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(.9f).background(Color.White)) {
                MainScreen(
                    viewModel,
                    searchViewModel,
                    permissionsService,
                    qrScannerStateViewModel
                )
                Box(
                    modifier = Modifier.padding(horizontal = 30.dp).fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    SavedToastMessage()
                }
            }
            Box(
                modifier = Modifier.weight(.1f).fillMaxWidth().background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ScreenProvider.getScreens().forEachIndexed { index, title ->
                        BottomTabCard(viewModel, index, title)
                    }
                }
            }
        }
        Statistics()
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    searchViewModel: SearchViewModel,
    permissionsService: PermissionsService,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    SavedNumberManagerPopupState.hidePopup()
    when (viewModel.currentScreen.value) {
        0 -> RandomScreen()
        1 -> RecommendationScreen()
        2 -> SaveScreen()
        3 -> SearchScreen(searchViewModel, permissionsService, qrScannerStateViewModel)
    }
}

@Composable
fun Statistics() {
    AnimatedVisibility(
        StatisticsState.show.value,
        enter = slideInVertically(),
        exit = slideOutVertically() + fadeOut()
    ) {
        StatisticsScreen()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomTabCard(
    viewModel: MainViewModel,
    index: Int,
    title: String,
) {
    AnimatedVisibility(
        visible = !StatisticsState.show.value,
        enter = slideInVertically(
            initialOffsetY = {
                it / 2
            },
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = {
                it / 2
            },
        ) + fadeOut(),
    ) {
        with(viewModel.currentScreen) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(width = 55.dp, height = 40.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = { viewModel.setScreen(index) },
                backgroundColor = if (value == index) {
                    colorResource(MR.colors.dark_gray_background)
                } else {
                    colorResource(MR.colors.white)
                },
                elevation = 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (value == index) {
                        Text(
                            text = title,
                            style = Body1(),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = title,
                            style = Body1(),
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

expect fun getPlatformName(): String
expect fun getHttpClient(): HttpClient

internal expect fun platformModule(): Module

val permissionsModule: Module = module {
    includes(platformModule())

    single<PermissionsService> {
        PermissionsServiceImpl()
    }
}

expect class CommonFlow<T>(flow: Flow<T>) : Flow<T>

fun <T> Flow<T>.toCommonFlow() = CommonFlow(this)

expect open class CommonMutableStateFlow<T>(flow: MutableStateFlow<T>) : MutableStateFlow<T>

fun <T> MutableStateFlow<T>.toCommonMutableStateFlow() = CommonMutableStateFlow(this)

expect open class CommonStateFlow<T>(flow: StateFlow<T>) : StateFlow<T>

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)