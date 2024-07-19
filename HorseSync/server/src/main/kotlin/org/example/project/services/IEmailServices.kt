package biblio.app.services

interface IEmailServices {

    suspend fun sendEmail(to: String, body: String, subject: String): EmailResult
    suspend fun verifyEmail(email: String): EmailResult

}