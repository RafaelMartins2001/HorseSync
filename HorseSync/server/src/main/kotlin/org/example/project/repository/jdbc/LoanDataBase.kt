package biblio.app.repository.jdbc

import biblio.app.repository.ILoanRepo
import domain.entities.Loan
import domain.primitive_basics.*
import java.sql.SQLException
import javax.sql.DataSource

class LoanDataBase(private val dataSource: DataSource) : ILoanRepo {

    override fun createLoan(
        lenderId: Identifier,
        borrowerId: Identifier,
        isbn: BookIdentifier,
        initialDate: Date,
        returnDate: Date
    ): Loan {
        val sqlQuery =
                "INSERT INTO loan (lender_id, borrower_id, isbn, initial_date, return_date) VALUES (?, ?, ?, TO_DATE(?, 'DD-MM-YYYY'), TO_DATE(?, 'DD-MM-YYYY')) RETURNING id"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, lenderId.value)
                preparedStatement.setInt(2, borrowerId.value)
                preparedStatement.setString(3, isbn.value)
                preparedStatement.setString(4, initialDate.value)
                preparedStatement.setString(5, returnDate.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val loanId = resultSet.getInt("id").toIdentifier()
                    Loan(loanId, borrowerId, lenderId, isbn, initialDate, returnDate)
                } else {
                    throw SQLException("Failed to insert loan into the database")
                }
            }
        }
    }

    override fun getLoanByBorrowerId(userId: Identifier): List<Loan> {
        val sqlQuery =
                "SELECT id, lender_id, borrower_id, isbn, TO_CHAR(initial_date, 'DD-MM-YYYY') as initial_date, TO_CHAR(return_date, 'DD-MM-YYYY') as return_date, state FROM loan WHERE borrower_id = ?"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                val loans = mutableListOf<Loan>()
                while (resultSet.next()) {
                    val loanId = resultSet.getInt("id").toIdentifier()
                    val lenderId = resultSet.getInt("lender_id").toIdentifier()
                    val borrowerId = resultSet.getInt("borrower_id").toIdentifier()
                    val isbn = resultSet.getString("isbn").toBookIdentifier()
                    val initialDate = resultSet.getString("initial_date").toDate()
                    val returnDate = resultSet.getString("return_date").toDate()
                    val state = Loan.State.valueOf(resultSet.getString("state"))
                    loans.add(Loan(loanId, borrowerId, lenderId, isbn, initialDate, returnDate, state))
                }
                loans
            }
        }
    }

    override fun getLoanByLenderId(userId: Identifier): List<Loan> {
        val sqlQuery =
                "SELECT id, borrower_id, lender_id, isbn, TO_CHAR(initial_date, 'DD-MM-YYYY') as initial_date, TO_CHAR(return_date, 'DD-MM-YYYY') as return_date, state FROM loan WHERE lender_id = ?"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                val loans = mutableListOf<Loan>()
                while (resultSet.next()) {
                    val loanId = resultSet.getInt("id").toIdentifier()
                    val lenderId = resultSet.getInt("lender_id").toIdentifier()
                    val borrowerId = resultSet.getInt("borrower_id").toIdentifier()
                    val isbn = resultSet.getString("isbn").toBookIdentifier()
                    val initialDate = resultSet.getString("initial_date").toDate()
                    val returnDate = resultSet.getString("return_date").toDate()
                    val state = Loan.State.valueOf(resultSet.getString("state"))
                    loans.add(Loan(loanId, borrowerId, lenderId, isbn, initialDate, returnDate, state))
                }
                loans
            }
        }
    }

    override fun returnBook(lenderId: Identifier, isbn: BookIdentifier): Loan? {
        val sqlQuery =
                "UPDATE loan SET state = 'CLOSED' WHERE lender_id = ? AND isbn = ? AND state = 'OPEN' RETURNING id, borrower_id, TO_CHAR(initial_date, 'DD-MM-YYYY') as initial_date, TO_CHAR(return_date, 'DD-MM-YYYY') as return_date"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, lenderId.value)
                preparedStatement.setString(2, isbn.value)
                val resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    val loanId = resultSet.getInt("id").toIdentifier()
                    val borrowerId = resultSet.getInt("borrower_id").toIdentifier()
                    val initialDate = resultSet.getString("initial_date").toDate()
                    val returnDate = resultSet.getString("return_date").toDate()
                    Loan(loanId, borrowerId, lenderId, isbn, initialDate, returnDate)
                } else {
                    null
                }
            }
        }
    }


    /* FUNÇÕES AUXILIARES */

    override fun isBorrower(userId: Identifier): Boolean {
        val sqlQuery = "SELECT id FROM loan WHERE borrower_id = ?"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun isLender(userId: Identifier): Boolean {
        val sqlQuery = "SELECT id FROM loan WHERE lender_id = ?"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, userId.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

    override fun isBookOccupied(isbn: BookIdentifier, lenderId: Identifier): Boolean {
        val sqlQuery = "SELECT id FROM loan WHERE isbn = ? AND lender_id = ? AND state = 'OPEN'"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setString(1, isbn.value)
                preparedStatement.setInt(2, lenderId.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next() // Verifica se há pelo menos uma linha
            }
        }
    }

    override fun isBorrowerInActiveLoan(borrower: Identifier): Boolean {
        val sqlQuery = "SELECT id FROM loan WHERE borrower_id = ? AND state = 'OPEN'"
        return executeDatabaseTransaction(dataSource) { connection ->
            connection.prepareStatement(sqlQuery).use { preparedStatement ->
                preparedStatement.setInt(1, borrower.value)
                val resultSet = preparedStatement.executeQuery()
                resultSet.next()
            }
        }
    }

}