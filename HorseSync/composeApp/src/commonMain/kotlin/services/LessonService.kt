package services

import io.ktor.client.*
import viewmodels.Lesson

data class LessonService(private val client: HttpClient) {
    fun getLessonsForMonth(month: Int) : List<Lesson> {
        TODO()
    }
    fun addLesson(lesson: Lesson) : Boolean {
        TODO()
    }
}