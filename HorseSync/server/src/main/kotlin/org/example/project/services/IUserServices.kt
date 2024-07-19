package biblio.app.services

import domain.entities.User
import domain.primitive_basics.*

interface IUserServices {
    fun createUser(name: Name, email: Email, password: Password): User
    fun login(email: Email, password: Password): User
    fun addBookToUser(userId: Identifier, isbn: BookIdentifier, bookId: BookIdentifier)
    fun removeBookFromUser(userId: Identifier, isbn: BookIdentifier)
    fun getUserBooks(userId: Identifier): List<BookIdentifier>
    fun getUsersWithThisBook(isbn: BookIdentifier): List<User>
    fun getNumberOfUserLoans(userId: Identifier): Int
    fun validateUser(userId: Identifier, email: Email): Boolean
    fun getUserByName(name: Name): User
}