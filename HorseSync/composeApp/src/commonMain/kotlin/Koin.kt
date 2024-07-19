import androidx.lifecycle.viewmodel.compose.viewModel
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.dsl.module
import services.AuthService
import services.LessonService
import viewmodels.AuthViewModel
import viewmodels.LessonViewModel


fun commonModule(enableNetworkLogs: Boolean = false) = module {
    single { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkLogs) }
    single { AuthService(get()) }
    single { LessonService(get()) }
    viewModel { AuthViewModel() }
    viewModel { LessonViewModel() }
}


fun createJson() = kotlinx.serialization.json.Json { isLenient = true; ignoreUnknownKeys = true }

fun createHttpClient(json: kotlinx.serialization.json.Json, enableNetworkLogs: Boolean) = io.ktor.client.HttpClient {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json(json)
    }
    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.NONE
        }
    }
}
