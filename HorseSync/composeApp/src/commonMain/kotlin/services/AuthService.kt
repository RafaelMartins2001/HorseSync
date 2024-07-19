package services

import io.ktor.client.*

data class AuthService(private val client: HttpClient) {
    fun login(username: String, password: String): Boolean {
        TODO( "Not yet implemented")
    }

    fun signUp(username: String, password: String, email: String): Boolean {
        TODO("Not yet implemented")
    }
}