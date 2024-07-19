package biblio.app.services


class SameNameException(message: String) : Exception(message)
class SameEmailException(message: String) : Exception(message)
class InvalidBookException(message: String) : Exception(message)
class BookAlreadyOwnedException(message: String) : Exception(message)
class BookCurrentlyBorrowedException(message: String) : Exception(message)
class BookNotReturnedException(message: String) : Exception(message)
class BookNotBorrowedException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class InternalServerErrorException(message: String) : Exception(message)
class BadLoginException(message: String) : Exception(message)