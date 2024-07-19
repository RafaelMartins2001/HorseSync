import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import screens.*

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object SignUp : Screen("signup")
    data object Home : Screen("home")
    data object Schedule : Screen("schedule")
    data object Calendar : Screen("calendar")
    data object Payments : Screen("payments")
    data object Admin : Screen("admin")
    data object Onboarding : Screen("onboarding")
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(route = Screen.Login.route) { LoginScreen(navController) }
        composable(route = Screen.SignUp.route) { SignUpScreen(navController) }
        composable(route = Screen.Home.route) { HomeScreen(navController) }
        composable(route = Screen.Schedule.route) { ScheduleScreen(navController) }
        composable(route = Screen.Calendar.route) { CalendarScreen(navController) }
        composable(route = Screen.Payments.route) { PaymentsScreen(navController) }
        composable(route = Screen.Admin.route) { AdminScreen(navController) }
    }
}