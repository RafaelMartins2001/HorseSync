package biblio.app.repository

import domain.entities.User
import domain.primitive_basics.*
import org.postgresql.ds.PGSimpleDataSource

interface IUserRepo {

    fun createUser(name: Name, email: Email, password: Password): User
    fun login(email: Email, password: Password): User?
    fun addBookToUser(userId: Identifier, isbn: BookIdentifier, bookId: BookIdentifier)
    fun removeBookFromUser(userId: Identifier, isbn: BookIdentifier)
    fun getUserBooks(userId: Identifier): List<BookIdentifier>
    fun getUsersWithThisBook(isbn: BookIdentifier): List<User>
    fun getNumberOfUserLoans(userId: Identifier): Int
    fun validateUser(userId: Identifier, email: Email): Boolean
    fun getUserByName(name: Name): User?
    fun cleanupBooks()

                    /* FUNÇÕES AUXILIARES */
    fun isValidLogin(email: Email, password: Password): Boolean
    fun userExistsByName(name: Name): Boolean
    fun userExistsByEmail(email: Email): Boolean
    fun userExistsById(id: Identifier): Boolean
    fun hasBook(userId: Identifier, isbn: BookIdentifier): Boolean
    fun isUniqueBook(isbn: BookIdentifier, bookId: BookIdentifier): Boolean
}