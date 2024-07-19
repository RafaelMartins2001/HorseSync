package biblio.app.repository.jdbc

import biblio.app.repository.IUserRepo
import domain.entities.User
import domain.primitive_basics.*
import org.postgresql.ds.PGSimpleDataSource
import java.sql.SQLException
import javax.sql.DataSource

class UserDataBase(private val dataSource: DataSource) : IUserRepo {

    override fun createUser(name: Name, email: Email, password: Password): User {
        val sqlQuery = "INSERT INTO users (name, email, password) VALUES (?, ?, ?) RETURNING id"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, name.value)
                preparedStatement.setString(2, email.value)
                preparedStatement.setString(3, password.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val userId = resultSet.getInt("id").toIdentifier()
                    User(userId, name, email, password)
                } else {
                    throw SQLException("Failed to insert user into the database")
                }
            }
        }
    }

    override fun login(email: Email, password: Password): User? {
        val sqlQuery = "SELECT * FROM users WHERE email = ? AND password = ? LIMIT 1"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, email.value)
                preparedStatement.setString(2, password.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val userId = resultSet.getInt("id").toIdentifier()
                    val name = resultSet.getString("name").toName()
                    User(userId, name, email, password)
                } else {
                    null
                }
            }
        }
    }

    override fun addBookToUser(userId: Identifier, isbn: BookIdentifier, bookId: BookIdentifier) {
        val sqlQuery = "SELECT add_book_to_user_inventory(?, ?, ?)"

        executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                preparedStatement.setString(2, isbn.value)
                preparedStatement.setString(3, bookId.value)
                preparedStatement.executeQuery()
            }
        }
    }

    override fun removeBookFromUser(userId: Identifier, isbn: BookIdentifier) {
        val sqlQuery = "DELETE FROM UserBooks WHERE user_id = ? AND isbn = ?"
        executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                preparedStatement.setString(2, isbn.value)
                preparedStatement.executeUpdate()
            }
        }
    }

    override fun getUserBooks(userId: Identifier): List<BookIdentifier> {
        val sqlQuery = "SELECT isbn FROM UserBooks WHERE user_id = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                val books = mutableListOf<BookIdentifier>()
                while (resultSet.next()) {
                    val isbn = resultSet.getString("isbn").toBookIdentifier()
                    books.add(isbn)
                }
                if (books.isEmpty()) {
                    return emptyList()
                }
                books
            }
        }
    }

    override fun getUsersWithThisBook(isbn: BookIdentifier): List<User> {
        val sqlQuery = "SELECT * FROM users JOIN UserBooks ON users.id = UserBooks.user_id WHERE isbn = ?"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, isbn.value)
                val resultSet = preparedStatement.executeQuery()
                val users = mutableListOf<User>()
                while (resultSet.next()) {
                    val userId = resultSet.getInt("id").toIdentifier()
                    val name = resultSet.getString("name").toName()
                    val email = resultSet.getString("email").toEmail()
                    val password = resultSet.getString("password").toPassword()
                    users.add(User(userId, name, email, password))
                }
                if (users.isEmpty()) {
                    return emptyList()
                }
                users
            }
        }
    }

    override fun getNumberOfUserLoans(userId: Identifier): Int {
        val sqlQuery = "SELECT COUNT(*) FROM Loan WHERE lender_id = ?"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
                resultSet.getInt(1)
            }
        }
    }


     override fun cleanupBooks() {
        val connection = dataSource.connection
        val statement = connection.createStatement()
        statement.execute("SELECT cleanup_books()")
        statement.close()
        connection.close()
    }

    override fun getUserByName(name: Name): User? {
        val sqlQuery = "SELECT * FROM users WHERE name = ? LIMIT 1"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, name.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val userId = resultSet.getInt("id").toIdentifier()
                    val email = resultSet.getString("email").toEmail()
                    val password = resultSet.getString("password").toPassword()
                    User(userId, name, email, password)
                } else {
                    null
                }
            }
        }
    }

    override fun validateUser(userId: Identifier, email: Email): Boolean {
        val sqlQuery = "SELECT * FROM users WHERE id = ? AND email = ? LIMIT 1"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                preparedStatement.setString(2, email.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }


    /* FUNÇÕES AUXILIARES */
    override fun isValidLogin(email: Email, password: Password): Boolean {
        val sqlQuery = "SELECT * FROM users WHERE email = ? AND password = ? LIMIT 1"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, email.value)
                preparedStatement.setString(2, password.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun userExistsByName(name: Name): Boolean {
        val sqlQuery = "SELECT * FROM users WHERE name = ? LIMIT 1"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, name.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun userExistsByEmail(email: Email): Boolean {
        val sqlQuery = "SELECT * FROM users WHERE email = ? LIMIT 1"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, email.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun userExistsById(id: Identifier): Boolean {
        val sqlQuery = "SELECT * FROM users WHERE id = ? LIMIT 1"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, id.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun hasBook(userId: Identifier, isbn: BookIdentifier): Boolean {
        val sqlQuery = "SELECT * FROM UserBooks WHERE user_id = ? AND isbn = ? LIMIT 1"
        return dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                preparedStatement.setString(2, isbn.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun isUniqueBook(isbn: BookIdentifier, bookId: BookIdentifier): Boolean {
        val sqlQueryIsbn = "SELECT * FROM book WHERE isbn = ? AND id != ? LIMIT 1"
        val sqlQueryBookId = "SELECT * FROM book WHERE isbn != ? AND id = ? LIMIT 1"

        dataSource.connection.use { connection ->
            connection.prepareStatement(sqlQueryIsbn).use { preparedStatement ->
                preparedStatement.setString(1, isbn.value)
                preparedStatement.setString(2, bookId.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    return false
                }
            }
            connection.prepareStatement(sqlQueryBookId).use { preparedStatement ->
                preparedStatement.setString(1, isbn.value)
                preparedStatement.setString(2, bookId.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    return false
                }
            }
        }

        return true
    }
}