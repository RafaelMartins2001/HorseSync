package screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.ktor.websocket.*
import viewmodels.AuthViewModel
import viewmodels.SignUpState

@Composable
fun SignUpScreen(navController: NavController, viewModel: AuthViewModel = koinViewModel()) {
    val signUpState by viewModel.signUpState.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Frame.Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Frame.Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Frame.Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.signUp(username, password, email) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Frame.Text("Sign Up")
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (signUpState) {
            is SignUpState.Loading -> CircularProgressIndicator()
            is SignUpState.Error -> Text((signUpState as SignUpState.Error).message, color = MaterialTheme.colors.error)
            is SignUpState.Success -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            }
            else -> {}
        }
    }
}