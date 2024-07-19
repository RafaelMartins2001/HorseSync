package screens

// OnboardingScreen.kt
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import horsesync.composeapp.generated.resources.Res
import horsesync.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.skia.ColorChannel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pages = listOf(
        OnboardingPage(
            title = "Welcome",
            description = "Welcome to Horse Riding School App! Your journey to mastering horse riding starts here.",
            imagePainter = painterResource(Res.drawable.compose_multiplatform)
        ),
        OnboardingPage(
            title = "Schedule Lessons",
            description = "Easily schedule your horse riding lessons and keep track of your progress.",
            imagePainter = painterResource(Res.drawable.compose_multiplatform)
        ),
        OnboardingPage(
            title = "Manage Payments",
            description = "View and manage your lesson payments effortlessly.",
            imagePainter = painterResource(Res.drawable.compose_multiplatform)
        )
    )

    var currentPage by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        HorizontalPager(
            state = rememberPagerState(initialPage = 0, pageCount = { pages.size }),
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageView(page = pages[page])
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pages.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .padding(horizontal = 4.dp)
                        .background(if (index == currentPage) Color.Blue else Color.Gray, RoundedCornerShape(5.dp))
                )
            }
        }

        Button(
            onClick = {
                if (currentPage < pages.size - 1) {
                    currentPage++
                } else {
                    navController.navigate("login")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = if (currentPage < pages.size - 1) "Next" else "Get Started", color = Color.White)
        }

        TextButton(
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Skip", color = Color.Blue)
        }
    }
}

@Composable
fun OnboardingPageView(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = page.imagePainter,
            contentDescription = page.title,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )
        Text(
            text = page.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = page.description,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

data class OnboardingPage(
    val title: String,
    val description: String,
    val imagePainter: Painter
)
