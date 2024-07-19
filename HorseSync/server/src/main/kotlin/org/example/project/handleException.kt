import biblio.app.services.*
import biblio.app.services.NotFoundException
import biblio.app.web.HttpStatusException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

suspend fun handleException(call: ApplicationCall, cause: Throwable) {
    val logger = LoggerFactory.getLogger("Application")
    val statusCode: HttpStatusCode
    val message: String

    when (cause) {
        is HttpStatusException -> {
            statusCode = cause.status
            message = cause.message ?: "Unknown error"
        }
        is NotFoundException -> {
            statusCode = HttpStatusCode.NotFound
            message = cause.message ?: "Not found"
        }
        is SameNameException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Name already exists"
        }
        is SameEmailException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Email already exists"
        }
        is InvalidBookException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Invalid book"
        }
        is BadLoginException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Bad login"
        }
        is BookCurrentlyBorrowedException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Book is currently borrowed"
        }
        is BookNotReturnedException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Book not returned"
        }
        is BookNotBorrowedException -> {
            statusCode = HttpStatusCode.NotFound
            message = cause.message ?: "Book not borrowed"
        }
        is BookAlreadyOwnedException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Book already owned"
        }
        is IllegalArgumentException -> {
            statusCode = HttpStatusCode.BadRequest
            message = cause.message ?: "Invalid argument"
        }
        else -> {
            statusCode = HttpStatusCode.InternalServerError
            message = "Internal server error"
        }
    }
    logger.error(message, cause)
    call.respond(statusCode, message)
}