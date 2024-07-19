package biblio.app.repository.localData

import biblio.app.repository.ILoanRepo
import domain.entities.Loan
import domain.primitive_basics.BookIdentifier
import domain.primitive_basics.Date
import domain.primitive_basics.Identifier
import java.util.concurrent.atomic.AtomicInteger


class LoanData : ILoanRepo {
    private val loans = mutableListOf<Loan>()
    private val idGenerator = AtomicInteger(0)
    override fun createLoan(
        lenderId: Identifier,
        borrowerId: Identifier,
        isbn: BookIdentifier,
        initialDate: Date,
        returnDate: Date
    ): Loan {
        val loan =
                Loan(Identifier(idGenerator.incrementAndGet()), lenderId, borrowerId, isbn, initialDate, returnDate)
        loans.add(loan)
        return loan
    }

    override fun getLoanByBorrowerId(userId: Identifier): List<Loan> {
        return loans.filter { it.borrowerId == userId }
    }

    override fun getLoanByLenderId(userId: Identifier): List<Loan> {
        return loans.filter { it.lenderId == userId }
    }

    override fun returnBook(lenderId: Identifier, isbn: BookIdentifier): Loan? {
        val loan = loans.find { it.lenderId == lenderId && it.isbn == isbn && it.state == Loan.State.OPEN }
        return loan?.copy(state = Loan.State.CLOSED)
    }


    /* Funções auxiliares  */
    override fun isBorrower(userId: Identifier): Boolean {
        return loans.any { it.borrowerId == userId }
    }

    override fun isLender(userId: Identifier): Boolean {
        return loans.any { it.lenderId == userId }
    }

    override fun isBookOccupied(isbn: BookIdentifier, lenderId: Identifier): Boolean {
        return loans.any {
            it.isbn == isbn &&
                    it.lenderId == lenderId &&
                    it.state == Loan.State.OPEN
        }
    }

    override fun isBorrowerInActiveLoan(borrower: Identifier): Boolean {
        return loans.any {
            it.borrowerId == borrower &&
                    it.state == Loan.State.OPEN
        }
    }


    /* FAZER DEPOIS */

    /*
        fun requestBook(userId: Identifier, bookId: BookIdentifier): Loan {
            val initialDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            return createLoan(userId, userId, bookId, initialDate.date, initialDate.date.plus(normalTerm, DateTimeUnit.DAY));
        }

        fun extendLoanByLender (loanId: Identifier, extraDays: Int): Loan {
            val loan = getLoanById(loanId)
            loan.let {
                it!!.returnDate = it.returnDate.plus(extraDays, DateTimeUnit.DAY)
            }
            return loan!!
        }

        fun extendLoanByBorrower (loanId: Identifier, extraDays: Int): Loan {
            val loan = getLoanById(loanId)
            loan.let {
                it!!.returnDate = it.returnDate.plus(extraDays, DateTimeUnit.DAY)
            }
            return loan!!
        }

        fun getLoanByBorrowerId(userId: Identifier): List<Loan> {
            return loans.filter { it.borrowerId == userId }
        }

        fun getLoanByLenderId(userId: Identifier): List<Loan> {
            return loans.filter { it.lenderId == userId }
        }

        fun getLoanById(loanId: Identifier): Loan? {
            return loans.find { it.id == loanId }
        }

        fun getLoansByUserId(userId: Identifier): List<Loan> {
            return loans.filter { it.borrowerId == userId || it.lenderId == userId }
        }

        fun deleteLoan(loanId: Identifier): Boolean {
            return loans.removeIf { it.id == loanId }
        }
    */
}
