package biblio.app.services

import biblio.app.repository.IUserRepo
import domain.entities.User
import domain.primitive_basics.*

class UserServices(private val userData: IUserRepo): IUserServices{

    override fun createUser(name: Name, email: Email, password: Password): User {
        if (userData.userExistsByName(name)) {
            throw SameNameException("User with the same name already exists")
        }
        if (userData.userExistsByEmail(email)) {
            throw SameEmailException("User with the same email already exists")
        }
        return userData.createUser(name, email, password)
    }

    override fun login(email: Email, password: Password) = userData.login(email, password) ?: throw BadLoginException("Invalid email or password")

    override fun addBookToUser(userId: Identifier, isbn: BookIdentifier, bookId: BookIdentifier) {
        if (!userData.userExistsById(userId)) {
            throw NotFoundException("User does not exist")
        }
        if (!userData.isUniqueBook(isbn, bookId)) {
            throw InvalidBookException("Book already exists with another ISBN or bookId")
        }
        if (userData.hasBook(userId, isbn)) {
            throw BookAlreadyOwnedException("User already has this book")
        }
        userData.addBookToUser(userId, isbn, bookId)
    }

    override fun removeBookFromUser(userId: Identifier, isbn: BookIdentifier) {
        if (!userData.userExistsById(userId)) {
            throw NotFoundException("User does not exist")
        }
        if (!userData.hasBook(userId, isbn)) {
            throw NotFoundException("User does not have this book")
        }
        userData.removeBookFromUser(userId, isbn)
    }

    override fun getUserBooks(userId: Identifier) : List<BookIdentifier> {
        if (!userData.userExistsById(userId)) {
            throw NotFoundException("User does not exist")
        }
        return userData.getUserBooks(userId)
    }

    override fun getUsersWithThisBook(isbn: BookIdentifier) = userData.getUsersWithThisBook(isbn)

    override fun getNumberOfUserLoans(userId: Identifier): Int {
        if (!userData.userExistsById(userId)) {
            throw NotFoundException("User does not exist")
        }
        return userData.getNumberOfUserLoans(userId)
    }

    override fun getUserByName(name: Name) = userData.getUserByName(name) ?: throw NotFoundException("User not found")

    override fun validateUser(userId: Identifier, email: Email) = userData.validateUser(userId, email)
}
