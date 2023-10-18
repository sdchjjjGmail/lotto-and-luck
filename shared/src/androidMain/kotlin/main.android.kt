import androidx.compose.runtime.Composable
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel
import com.content.lottoandluck.permissions.delegate.PermissionDelegate
import com.content.lottoandluck.permissions.model.Permission
import com.content.lottoandluck.presenter.viewmodel.MainViewModel
import com.content.lottoandluck.presenter.viewmodel.SearchViewModel
import delegate.CameraPermissionDelegate
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import org.kodein.di.instance
import org.koin.core.Koin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual fun getPlatformName(): String = "Android"
actual fun getHttpClient(): HttpClient = HttpClient(OkHttp) {
    Napier.base(DebugAntilog())
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                useAlternativeNames = true
            })
    }
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Napier.i(tag = "ApiService", message = message)
            }
        }
    }
}

internal actual fun platformModule(): Module = module {
    single<PermissionDelegate>(named(Permission.CAMERA.name)) {
        CameraPermissionDelegate(
            context = get(),
            activity = inject(),
        )
    }
//    single {
//        get<BluetoothManager>().adapter
//    }
}

actual class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow

actual open class CommonMutableStateFlow<T> actual constructor(
    private val flow: MutableStateFlow<T>
) : MutableStateFlow<T> by flow

actual open class CommonStateFlow<T> actual constructor(
    private val flow: StateFlow<T>
) : StateFlow<T> by flow

@Composable
fun MainView(
    koin: Koin,
    qrScannerStateViewModel: QrScannerStateViewModel
) {
    val viewModel: MainViewModel by ViewModelFactory.viewModel.instance()
    val searchViewModel: SearchViewModel by ViewModelFactory.viewModel.instance()
    App(
        permissionsService = koin.get(),
        viewModel = viewModel,
        searchViewModel = searchViewModel,
        qrScannerStateViewModel = qrScannerStateViewModel
    )
}
