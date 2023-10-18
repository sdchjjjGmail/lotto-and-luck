import androidx.compose.ui.window.ComposeUIViewController
import com.content.lottoandluck.di.ViewModelFactory
import com.content.lottoandluck.di.initKoin
import com.content.lottoandluck.nativespecific.qr.QrScannerStateViewModel
import com.content.lottoandluck.permissions.delegate.PermissionDelegate
import com.content.lottoandluck.permissions.model.Permission
import com.content.lottoandluck.presenter.viewmodel.MainViewModel
import com.content.lottoandluck.presenter.viewmodel.SearchViewModel
import delegate.CameraPermissionDelegate
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.kodein.di.instance
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.AVFoundation.AVMediaTypeVideo
import platform.UIKit.UIViewController

actual fun getPlatformName(): String = "iOS"
actual fun getHttpClient(): HttpClient = HttpClient(Darwin) {
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
        CameraPermissionDelegate(AVMediaTypeVideo, Permission.CAMERA)
    }
}

actual open class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow {

    // Collects values emitted by the flow
    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        onCollect: (T) -> Unit
    ): DisposableHandle {
        val job = coroutineScope.launch(dispatcher) {
            flow.collect(onCollect)
        }
        return DisposableHandle { job.cancel() }
    }

    // Shorthand for the first method
    fun subscribe(
        onCollect: (T) -> Unit
    ): DisposableHandle {
        return subscribe(
            coroutineScope = GlobalScope,
            dispatcher = Dispatchers.Main,
            onCollect = onCollect
        )
    }
}

actual open class CommonMutableStateFlow<T> actual constructor(
    private val flow: MutableStateFlow<T>
) : CommonStateFlow<T>(flow), MutableStateFlow<T> {

    override val replayCache: List<T>
        get() = flow.replayCache

    override val subscriptionCount: StateFlow<Int>
        get() = flow.subscriptionCount

    override var value: T
        get() = super.value
        set(value) {
            flow.value = value
        }

    override fun compareAndSet(expect: T, update: T): Boolean {
        return flow.compareAndSet(expect, update)
    }

    @ExperimentalCoroutinesApi
    override fun resetReplayCache() {
        flow.resetReplayCache()
    }

    override fun tryEmit(value: T): Boolean {
        return flow.tryEmit(value)
    }

    override suspend fun emit(value: T) {
        flow.emit(value)
    }

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        flow.collect(collector)
    }
}

actual open class CommonStateFlow<T> actual constructor(
    private val flow: StateFlow<T>
) : CommonFlow<T>(flow), StateFlow<T> {

    override val replayCache: List<T>
        get() = flow.replayCache

    override val value: T
        get() = flow.value

    override suspend fun collect(collector: FlowCollector<T>): Nothing {
        flow.collect(collector)
    }
}

class IOSMutableStateFlow<T>(
    initialValue: T
) : CommonMutableStateFlow<T>(MutableStateFlow(initialValue))

@Suppress("unused")
fun interface DisposableHandle : DisposableHandle

@Suppress("FunctionName", "unused")
fun MainViewController(qrScannerStateViewModel: QrScannerStateViewModel): UIViewController {
    val viewModel: MainViewModel by ViewModelFactory.viewModel.instance()
    val searchViewModel: SearchViewModel by ViewModelFactory.viewModel.instance()
    val koin = initKoin().koin

    return ComposeUIViewController {
        App(
            permissionsService = koin.get(),
            viewModel = viewModel,
            searchViewModel = searchViewModel,
            qrScannerStateViewModel = qrScannerStateViewModel
        )
    }
}