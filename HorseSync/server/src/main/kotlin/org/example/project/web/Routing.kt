package biblio.app.web

import biblio.app.services.*
import io.ktor.server.routing.*

fun Route.emailAPI(emailServices: IEmailServices) {
    val emailAPI = EmailAPI(emailServices)
    route("/lender/email/send") {
        post {
            emailAPI.sendEmail(call)
        }
    }
    route("/lender/email/verify/{email}") {
        get {
            emailAPI.verifyEmail(call)
        }
    }
}

fun Route.userAPI(userServices: IUserServices) {
    val userAPI = UserAPI(userServices)
    route("/users") {
        post {
            userAPI.createUser(call)
        }
    }

    route("/login") {
        post {
            userAPI.login(call)
        }
    }

    route("/users/books") {
        post {
            userAPI.addBookToUser(call)
        }

        delete {
            userAPI.removeBookFromUser(call)
        }
    }

    route("/users/{id}/books") {
        get {
            userAPI.getUserBooks(call)
        }
    }

    route("/usersbooks/{isbn}") {
        get {
            userAPI.getUsersWithThisBook(call)
        }
    }

    route("/users/loans/{id}") {
        get {
            userAPI.getNumberOfUserLoans(call)
        }
    }

    route("/users/exists") {
        post {
            userAPI.doesUserExist(call)
        }
    }

    route("/userByName/{name}") {
        get {
            userAPI.getUserByName(call)
        }
    }
}

fun Route.loanAPI(loanServices: ILoanServices) {
    val loanAPI = LoanAPI(loanServices)
    route("/loans") {
        post {
            loanAPI.createLoan(call)
        }
    }

    route("/loans/borrower/{id}") {
        get {
            loanAPI.getLoansByBorrowerId(call)
        }
    }

    route("/loans/lender/{id}") {
        get {
            loanAPI.getLoansByLenderId(call)
        }
    }

    route("/loans/return/{lenderId}/{isbn}") {
        post {
            loanAPI.returnBook(call)
        }
    }
}
