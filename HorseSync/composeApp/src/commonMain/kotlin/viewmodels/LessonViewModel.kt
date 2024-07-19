package viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import services.LessonService
import java.time.LocalDate
import java.time.LocalTime

class LessonViewModel : ViewModel(), KoinComponent {
    private val lessonService : LessonService by inject()
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons

    fun loadLessons(month: Int) {
        viewModelScope.launch {
            _lessons.value = lessonService.getLessonsForMonth(month)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addLesson(lesson: Lesson) {
        viewModelScope.launch {
            val success = lessonService.addLesson(lesson)
            if (success) {
                // Reload lessons if the lesson was added successfully
                loadLessons(lesson.date.withDayOfMonth(1).monthValue)
            }
        }
    }
}

data class Lesson(
    val id: String,
    val date: LocalDate,
    val time: LocalTime,
    val studentName: String,
    val isPaid: Boolean
)
