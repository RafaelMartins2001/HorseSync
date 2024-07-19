package biblio.app.services

import biblio.app.repository.ILoanRepo
import biblio.app.repository.IUserRepo
import domain.entities.Loan
import domain.primitive_basics.BookIdentifier
import domain.primitive_basics.Date
import domain.primitive_basics.Identifier

class LoanServices(private val loanData: ILoanRepo, private val userData: IUserRepo): ILoanServices{

    override fun createLoan (
        lenderId: Identifier,
        borrowerId: Identifier,
        isbn: BookIdentifier,
        initialDate: Date,
        returnDate: Date
    ) {
        if (lenderId == borrowerId) {
            throw IllegalArgumentException("The lender and borrower cannot be the same.")
        }
        if (!userData.userExistsById(lenderId)) {
            throw NotFoundException("User not found.")
        }
        if (!userData.userExistsById(borrowerId)) {
            throw NotFoundException("User not found.")
        }
        if (!userData.hasBook(lenderId, isbn)) {
            throw NotFoundException("User does not have this book.")
        }
        if (loanData.isBookOccupied(isbn, lenderId)) {
            throw BookCurrentlyBorrowedException("The book is currently borrowed.")
        }
        if (loanData.isBorrowerInActiveLoan(borrowerId)) {
            throw BookNotReturnedException("The borrower already has an active loan and must return the book before issuing a new loan.")
        }
        loanData.createLoan(lenderId, borrowerId, isbn, initialDate, returnDate)
    }

    override fun getLoansByBorrowerId(borrowerId: Identifier): List<Loan> {
        if (!userData.userExistsById(borrowerId)) {
            throw NotFoundException("User not found.")
        }
        if (!loanData.isBorrower(borrowerId)) {
            throw NotFoundException("This user has not borrowed any books.")
        }
        return loanData.getLoanByBorrowerId(borrowerId)
    }

    override fun getLoansByLenderId(lenderId: Identifier): List<Loan> {
        if (!userData.userExistsById(lenderId)) {
            throw NotFoundException("User not found.")
        }
        if (!loanData.isLender(lenderId)) {
            throw NotFoundException("The user has not lent any books.")
        }
        return loanData.getLoanByLenderId(lenderId)
    }

    override fun returnBook(lenderId: Identifier, isbn: BookIdentifier): Loan? {
        if (!userData.userExistsById(lenderId)) {
            throw NotFoundException("User not found.")
        }
        if (!loanData.isBookOccupied(isbn, lenderId)) {
            throw BookNotBorrowedException("The book is not borrowed by this user.")
        }
        return loanData.returnBook(lenderId, isbn)
    }
}
