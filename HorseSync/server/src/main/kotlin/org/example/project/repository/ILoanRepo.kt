package biblio.app.repository

import domain.entities.Loan
import domain.primitive_basics.BookIdentifier
import domain.primitive_basics.Date
import domain.primitive_basics.Identifier

interface ILoanRepo {
    fun createLoan(
        lenderId: Identifier,
        borrowerId: Identifier,
        isbn: BookIdentifier,
        initialDate: Date,
        returnDate: Date
    ): Loan

    fun getLoanByBorrowerId(userId: Identifier): List<Loan>
    fun getLoanByLenderId(userId: Identifier): List<Loan>
    fun returnBook(lenderId: Identifier, isbn: BookIdentifier): Loan?

                /* FUNÇÕES AUXILIARES */
    fun isBorrower(userId: Identifier): Boolean
    fun isLender(userId: Identifier): Boolean
    fun isBookOccupied(isbn: BookIdentifier, lenderId: Identifier): Boolean
    fun isBorrowerInActiveLoan(borrower: Identifier): Boolean
}