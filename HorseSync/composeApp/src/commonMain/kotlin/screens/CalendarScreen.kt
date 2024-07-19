import android.widget.CalendarView
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate

@Composable
fun CalendarScreen() {
    // State to hold selected date
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val events = remember { mutableStateListOf<CalendarEvent>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calendar", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(
            modifier = Modifier.fillMaxSize(),
            selectedDate = selectedDate,
            onDateSelected = { date ->
                selectedDate = date
                // Optionally handle date selection
            },
            events = events // Provide events if you have any
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Selected Date: $selectedDate")
    }
}
