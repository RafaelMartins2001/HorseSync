package biblio.app.web

import io.ktor.http.*

open class HttpStatusException(val status: HttpStatusCode, message: String) : RuntimeException(message)
class InvalidParameterException(message: String) : HttpStatusException(HttpStatusCode.BadRequest, message)

