
import biblio.app.repository.ILoanRepo
import biblio.app.repository.IUserRepo
import biblio.app.repository.jdbc.LoanDataBase
import biblio.app.repository.jdbc.UserDataBase
import biblio.app.services.*
import biblio.app.web.emailAPI
import biblio.app.web.loanAPI
import biblio.app.web.userAPI
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.routing.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.postgresql.ds.PGSimpleDataSource
import java.util.concurrent.TimeUnit

fun main() {
    embeddedServer(Netty, port = 1906, host = "localhost", module = Application::module)
        .start(wait = true)
}

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    install(StatusPages) {
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                handleException(call, cause)
            }
        }
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Post)
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        })
    }
    val dataSource = PGSimpleDataSource()
    val jdbcDatabaseURL = System.getenv("JDBC_DATABASE_URL_BIBLIOBYTE")
    dataSource.setURL(jdbcDatabaseURL)

    val userData: IUserRepo = UserDataBase(dataSource)
    val userServices: IUserServices = UserServices(userData)
    val loanData: ILoanRepo = LoanDataBase(dataSource)
    val loanServices: ILoanServices = LoanServices(loanData, userData)
    val emailServices: IEmailServices = EmailServices()

    GlobalScope.launch {
        while (isActive) {
            userData.cleanupBooks()
            kotlinx.coroutines.delay(TimeUnit.HOURS.toMillis(48))
        }
    }
    routing {
        loanAPI(loanServices)
        userAPI(userServices)
        emailAPI(emailServices)
    }
}
