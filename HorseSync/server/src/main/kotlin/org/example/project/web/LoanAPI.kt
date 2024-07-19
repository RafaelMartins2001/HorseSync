package biblio.app.web

import biblio.app.services.ILoanServices
import domain.primitive_basics.toBookIdentifier
import domain.primitive_basics.toDate
import domain.primitive_basics.toIdentifier
import dto.loans.LoanListResponseDTO
import dto.loans.LoanRequestDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*

class LoanAPI(private val loanServices: ILoanServices) {

    suspend fun createLoan(call: ApplicationCall) {
        val loanRequestDTO = call.receive<LoanRequestDTO>()
        val lenderId = loanRequestDTO.lenderId.toIdentifier()
        val borrowerId = loanRequestDTO.borrowerId.toIdentifier()
        val isbn = loanRequestDTO.isbn.toBookIdentifier()
        val initialDate = loanRequestDTO.initialDate.toDate()
        val returnDate = loanRequestDTO.returnDate.toDate()

        loanServices.createLoan(lenderId, borrowerId, isbn, initialDate, returnDate)
        call.respond(HttpStatusCode.Created, "Loan created successfully")
    }

    suspend fun getLoansByBorrowerId(call: ApplicationCall) {
        val borrowerId = call.parameters["id"]?.toIdentifier() ?: throw BadRequestException("Invalid borrower ID")
        val loans = loanServices.getLoansByBorrowerId(borrowerId)
        call.respond(HttpStatusCode.OK, LoanListResponseDTO(loans.map { it.toLoanDTO() }))
    }

    suspend fun getLoansByLenderId(call: ApplicationCall) {
        val lenderId = call.parameters["id"]?.toIdentifier() ?: throw BadRequestException("Invalid lender ID")
        val loans = loanServices.getLoansByLenderId(lenderId)
        call.respond(HttpStatusCode.OK, LoanListResponseDTO(loans.map { it.toLoanDTO() }))
    }

    suspend fun returnBook(call: ApplicationCall) {
        val lenderId = call.parameters["lenderId"]?.toIdentifier() ?: throw BadRequestException("Invalid lender ID")
        val isbn = call.parameters["isbn"]?.toBookIdentifier() ?: throw BadRequestException("Invalid book")

        loanServices.returnBook(lenderId, isbn)
        call.respond(HttpStatusCode.OK, "Book returned successfully")
    }
}
