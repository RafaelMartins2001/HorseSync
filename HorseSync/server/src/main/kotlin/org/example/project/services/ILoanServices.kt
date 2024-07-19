package biblio.app.services

import domain.entities.Loan
import domain.primitive_basics.BookIdentifier
import domain.primitive_basics.Date
import domain.primitive_basics.Identifier

interface ILoanServices {
    fun createLoan(lenderId: Identifier, borrowerId: Identifier, isbn: BookIdentifier, initialDate: Date, returnDate: Date)
    fun getLoansByBorrowerId(borrowerId: Identifier): List<Loan>
    fun getLoansByLenderId(lenderId: Identifier): List<Loan>
    fun returnBook(lenderId: Identifier, isbn: BookIdentifier): Loan?
}