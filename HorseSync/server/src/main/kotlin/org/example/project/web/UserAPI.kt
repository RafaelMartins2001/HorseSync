package biblio.app.web

import biblio.app.services.IUserServices
import domain.entities.toDTO
import domain.primitive_basics.*
import dto.books.BookOperationsResponseDTO
import dto.books.NewBookRequestDTO
import dto.books.UserBooksDTO
import dto.users.UserExistsRequestDTO
import dto.users.UserRequestCreateDTO
import dto.users.UserRequestLoginDTO
import dto.users.UsersResponseDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class UserAPI(private val userServices: IUserServices) {

    suspend fun createUser(call: ApplicationCall) {
        val userCreateDTO = call.receive<UserRequestCreateDTO>()
        val name = userCreateDTO.name.toName()
        val email = userCreateDTO.email.toEmail()
        val password = userCreateDTO.password.toPassword()

        val user = userServices.createUser(name, email, password)
        call.respond(HttpStatusCode.Created, user.toDTO())
    }

    /*
     * In this function, we need to use a try-catch block as it was the only approach we found to handle
     * the error message during login. In cases where an email or password is invalid,
     * an exception is thrown at runtime. We immediately capture and manipulate it to ensure
     * that for security reasons, the message is not overly descriptive.
     */
    suspend fun login(call: ApplicationCall) {
        try {
            val userLoginDTO = call.receive<UserRequestLoginDTO>()
            val email = userLoginDTO.email.toEmail()
            val password = userLoginDTO.password.toPassword()
            val user = userServices.login(email, password)
            call.respond(HttpStatusCode.OK, user.toDTO())
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, "Invalid email or password")
        }
    }

    suspend fun addBookToUser(call: ApplicationCall) {
        val addBookDTO = call.receive<NewBookRequestDTO>()
        val userId = addBookDTO.userId.toIdentifier()
        val isbn = addBookDTO.isbn.toBookIdentifier()
        val bookId = addBookDTO.bookId.toBookIdentifier()

        userServices.addBookToUser(userId, isbn, bookId)
        call.respond(HttpStatusCode.OK, "Book added to user inventory successfully")
    }

    suspend fun removeBookFromUser(call: ApplicationCall) {
        val removeBookDTO = call.receive<BookOperationsResponseDTO>()
        val userId = removeBookDTO.userId.toIdentifier()
        val isbn = removeBookDTO.isbn.toBookIdentifier()

        userServices.removeBookFromUser(userId, isbn)
        call.respond(HttpStatusCode.OK, "Book removed from user inventory successfully")
    }

    suspend fun getUserBooks(call: ApplicationCall) {
        val userId = call.parameters["id"]?.toIdentifier() ?: throw InvalidParameterException("Invalid user ID")
        val books = userServices.getUserBooks(userId)
        call.respond(HttpStatusCode.OK, UserBooksDTO(userId.value, books.map { it.value }))
    }

    suspend fun getUsersWithThisBook(call: ApplicationCall) {
        val isbn = call.parameters["isbn"]?.toBookIdentifier() ?: throw InvalidParameterException("Invalid ISBN")
        val users = userServices.getUsersWithThisBook(isbn)
        call.respond(HttpStatusCode.OK, UsersResponseDTO(users.map { it.toDTO() }))
    }

    suspend fun getNumberOfUserLoans(call: ApplicationCall) {
        val userId = call.parameters["id"]?.toIdentifier() ?: throw InvalidParameterException("Invalid user ID")
        val loans = userServices.getNumberOfUserLoans(userId)
        call.respond(HttpStatusCode.OK, loans)
    }

    suspend fun getUserByName(call: ApplicationCall) {
        val name = call.parameters["name"]?.toName() ?: throw InvalidParameterException("Invalid user name")
        val user = userServices.getUserByName(name)
        call.respond(HttpStatusCode.OK, user.toDTO())
    }

    suspend fun doesUserExist(call: ApplicationCall) {
        val userExist = call.receive<UserExistsRequestDTO>()
        val userId = userExist.userId.toIdentifier()
        val email = userExist.email.toEmail()
        if (userServices.validateUser(userId, email)) {
            call.respond(HttpStatusCode.OK, "User validated")
        } else {
            call.respond(HttpStatusCode.NotFound, "User not validated")
        }
    }
}
