package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.AuthService

class AuthViewModel : ViewModel(), KoinComponent {
    private val authService: AuthService by inject()
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Idle)
    val signUpState: StateFlow<SignUpState> = _signUpState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val success = authService.login(username, password)
            _loginState.value = if (success) LoginState.Success else LoginState.Error("Invalid credentials")
        }
    }

    fun signUp(username: String, password: String, email: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading
            val success = authService.signUp(username, password, email)
            _signUpState.value = if (success) SignUpState.Success else SignUpState.Error("Sign-up failed")
        }
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class SignUpState {
    object Idle : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}
