package biblio.app.web

import biblio.app.services.EmailResult
import biblio.app.services.IEmailServices
import biblio.app.services.InternalServerErrorException
import domain.primitive_basics.toEmail
import dto.emails.EmailRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

class EmailAPI(private val emailServices: IEmailServices) {

    private val logger = LoggerFactory.getLogger(EmailAPI::class.java)

    suspend fun sendEmail(call: ApplicationCall) {
        val emailRequestDTO = call.receive<EmailRequestDTO>()
        val to = emailRequestDTO.to
        val body = emailRequestDTO.body
        val subject = emailRequestDTO.subject

        if (to.isBlank() || body.isBlank() || subject.isBlank()) {
            throw InvalidParameterException("Email, body, and subject must not be blank")
        }

        val response = emailServices.sendEmail(to, body, subject)
        if (response is EmailResult.Success) {
            logger.info("Email sent successfully to $to")
            call.respond(HttpStatusCode.OK, "Email sent successfully")
        } else if (response is EmailResult.Failure) {
            logger.error("Failed to send email to $to: ${response.exception}")
            throw InternalServerErrorException("Failed to send email: ${response.exception}")
        }
    }

    suspend fun verifyEmail(call: ApplicationCall) {
        val email = call.parameters["email"]?.toEmail() ?: throw InvalidParameterException("Invalid email")
        val result = emailServices.verifyEmail(email.value)
        when (result) {
            is EmailResult.Success -> call.respond(HttpStatusCode.OK, "Email address is valid")
            is EmailResult.InvalidEmail -> call.respond(HttpStatusCode.BadRequest, "Invalid email address")
            is EmailResult.Failure -> call.respond(HttpStatusCode.InternalServerError, "Failed to verify email address")
        }
    }

}
