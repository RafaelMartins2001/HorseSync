package biblio.app.repository.localData


import biblio.app.repository.IUserRepo
import domain.entities.Book
import domain.entities.User
import domain.primitive_basics.*
import org.postgresql.ds.PGSimpleDataSource
import java.util.concurrent.atomic.AtomicInteger


class UserData : IUserRepo {

    private val users = mutableListOf<User>()
    private val books = mutableListOf<Book>()
    private val userBooks = mutableMapOf<Identifier, MutableList<BookIdentifier>>()
    private val idGenerator = AtomicInteger(0)

    override fun createUser(name: Name, email: Email, password: Password): User {
        val user = User(Identifier(idGenerator.incrementAndGet()), name, email, password)
        users.add(user)
        return user
    }

    override fun login(email: Email, password: Password) = users.find { it.email == email && it.password == password }

    override fun addBookToUser(userId: Identifier, isbn: BookIdentifier, bookId: BookIdentifier) {
        val bookExists = books.any { it.isbn == isbn }
        if (!bookExists) {
            books.add(Book(isbn, bookId))
        }
        userBooks.getOrPut(userId) { mutableListOf() }.add(isbn)
    }

    override fun removeBookFromUser(userId: Identifier, isbn: BookIdentifier) {
        userBooks[userId]?.remove(isbn)
    }

    override fun getUserBooks(userId: Identifier) = userBooks[userId] ?: emptyList()

    override fun getUsersWithThisBook(isbn: BookIdentifier) =
        userBooks.entries.filter { it.value.contains(isbn) }.mapNotNull { entry ->
            users.find { it.id == entry.key }
        }

    override fun getNumberOfUserLoans(userId: Identifier) = userBooks[userId]?.size ?: 0

    override fun getUserByName(name: Name) = users.find { it.name == name }

    override fun validateUser(userId: Identifier, email: Email) = users.any { it.id == userId && it.email == email }

    override fun cleanupBooks() {
        // Sem implementação, pois só faz sentido a nivel da base de dados
    }


                                                                             /* FUNÇÕES AUXILIARES */
    override fun isValidLogin(email: Email, password: Password) = users.any { it.email == email && it.password == password }

    override fun userExistsByName(name: Name) = users.any { it.name == name }

    override fun userExistsByEmail(email: Email) = users.any { it.email == email }

    override fun userExistsById(id: Identifier) = users.any { it.id == id }

    override fun hasBook(userId: Identifier, isbn: BookIdentifier) = userBooks[userId]?.contains(isbn) ?: false

    override fun isUniqueBook(isbn: BookIdentifier, bookId: BookIdentifier) = !(books.any { it.isbn == isbn && it.bookId != bookId } || books.any { it.isbn != isbn && it.bookId == bookId })

}
