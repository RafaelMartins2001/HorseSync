package biblio.app.services


import dto.emails.ZeroBounceValidationResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import net.axay.simplekotlinmail.delivery.mailerBuilder
import net.axay.simplekotlinmail.delivery.send
import net.axay.simplekotlinmail.email.emailBuilder
import org.slf4j.LoggerFactory

sealed class EmailResult {
    data object Success : EmailResult()
    data class Failure(val exception: Throwable) : EmailResult()
    data object InvalidEmail : EmailResult()
}

class EmailServices : IEmailServices {

    private val zeroBounceApiKey = "f628cdde5ca84a3ba204ba223a7a0f4c"
    private val zeroBounceValidationUrl = "https://api.zerobounce.net/v2/validate"

    private val client = HttpClient {
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
    }

    private val logger = LoggerFactory.getLogger(EmailServices::class.java)

    override suspend fun sendEmail(to: String, body: String, subject: String): EmailResult = withContext(Dispatchers.IO) {
        val email = emailBuilder {
            from("Bibliobyte Team", "duelistapro@gmail.com")
            to(to)
            withSubject(subject)
            withPlainText(body)
        }
        val mailer = mailerBuilder(
            host = "smtp-relay.brevo.com",
            port = 587,
            username = "771bed001@smtp-brevo.com",
            password = "mgswHLMxZNp27bfP"
        )
        return@withContext try {
            email.send(mailer)
            logger.info("Email sent successfully to $to")
            EmailResult.Success
        } catch (e: Exception) {
            logger.error("Failed to send email to $to", e)
            EmailResult.Failure(e)
        }
    }

    override suspend fun verifyEmail(email: String): EmailResult = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = client.get(zeroBounceValidationUrl) {
                parameter("api_key", zeroBounceApiKey)
                parameter("email", email)
            }

            if (response.status == HttpStatusCode.OK) {
                val validationResult = response.body<ZeroBounceValidationResponse>()
                if (validationResult.status == "valid") {
                    logger.info("Email address $email is valid")
                    EmailResult.Success
                } else {
                    logger.warn("Email address $email is invalid: ${validationResult.status}")
                    EmailResult.InvalidEmail
                }
            } else {
                throw Exception("Failed to verify email: ${response.status}")
            }
        } catch (e: Exception) {
            logger.error("Failed to verify email address $email", e)
            EmailResult.Failure(e)
        }
    }
}