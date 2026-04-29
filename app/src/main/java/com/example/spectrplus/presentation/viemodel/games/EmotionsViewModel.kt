package com.example.spectrplus.presentation.viemodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class EmotionQuestion(
    val id: Int,
    val emoji: String,
    val situation: String,
    val correctAnswer: String,
    val wrongAnswers: List<String>,
    val level: Int
)

@HiltViewModel
class EmotionsViewModel @Inject constructor() : ViewModel() {

    private val allQuestions = listOf(
        EmotionQuestion(1, "😊", "Мальчик получил подарок", "Радость", listOf("Грусть", "Злость", "Страх"), 1),
        EmotionQuestion(2, "😢", "Девочка потеряла игрушку", "Грусть", listOf("Радость", "Злость", "Удивление"), 1),
        EmotionQuestion(3, "😠", "Мальчик не хочет убирать игрушки", "Злость", listOf("Радость", "Грусть", "Страх"), 1),
        EmotionQuestion(4, "😨", "Ребёнок увидел страшную собаку", "Страх", listOf("Радость", "Злость", "Грусть"), 1),
        EmotionQuestion(5, "😮", "Девочка увидела фокус", "Удивление", listOf("Радость", "Страх", "Злость"), 2),
        EmotionQuestion(6, "🤢", "Мальчик съел что-то невкусное", "Отвращение", listOf("Страх", "Грусть", "Злость"), 2),
        EmotionQuestion(7, "😌", "Ребёнок лежит на диване после прогулки", "Спокойствие", listOf("Грусть", "Усталость", "Скука"), 2),
        EmotionQuestion(8, "😴", "Девочка очень хочет спать", "Усталость", listOf("Скука", "Спокойствие", "Грусть"), 2),
        EmotionQuestion(9, "😤", "Ребёнок сделал что-то сам", "Гордость", listOf("Злость", "Радость", "Удивление"), 3),
        EmotionQuestion(10, "🥺", "Мальчика не взяли играть", "Обида", listOf("Грусть", "Страх", "Удивление"), 3),
        EmotionQuestion(11, "😳", "Девочка сломала вазу случайно", "Смущение", listOf("Страх", "Удивление", "Грусть"), 3),
        EmotionQuestion(12, "🤩", "Ребёнок увидел салют", "Восхищение", listOf("Радость", "Удивление", "Гордость"), 3)
    )

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex = _currentIndex.asStateFlow()

    private val _selectedAnswer = MutableStateFlow<String?>(null)
    val selectedAnswer = _selectedAnswer.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect = _isCorrect.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    private val _currentOptions = MutableStateFlow<List<String>>(emptyList())
    val currentOptions = _currentOptions.asStateFlow()

    init {
        shuffleOptions()
    }

    val currentQuestion: EmotionQuestion get() = allQuestions[_currentIndex.value]
    val totalQuestions: Int get() = allQuestions.size

    private fun shuffleOptions() {
        val q = allQuestions[_currentIndex.value]
        _currentOptions.value = (listOf(q.correctAnswer) + q.wrongAnswers).shuffled()
    }

    fun selectAnswer(answer: String) {
        if (_selectedAnswer.value != null) return
        _selectedAnswer.value = answer
        val correct = answer == currentQuestion.correctAnswer
        _isCorrect.value = correct
        if (correct) _score.value++
    }

    fun nextQuestion() {
        val next = _currentIndex.value + 1
        if (next >= allQuestions.size) {
            _completed.value = true
        } else {
            _currentIndex.value = next
            _selectedAnswer.value = null
            _isCorrect.value = null
            shuffleOptions()
        }
    }

    fun restart() {
        _currentIndex.value = 0
        _selectedAnswer.value = null
        _isCorrect.value = null
        _score.value = 0
        _completed.value = false
        shuffleOptions()
    }
}
