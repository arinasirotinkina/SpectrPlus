package com.example.spectrplus.presentation.viemodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class SequenceStep(
    val id: Int,
    val emoji: String,
    val label: String,
    val correctPosition: Int
)

data class SequenceLevel(
    val title: String,
    val description: String,
    val steps: List<SequenceStep>
)

@HiltViewModel
class SequenceViewModel @Inject constructor() : ViewModel() {

    private val levels = listOf(
        SequenceLevel(
            "Умывание",
            "Разложи шаги умывания по порядку",
            listOf(
                SequenceStep(1, "🚿", "Открыть воду", 0),
                SequenceStep(2, "🧼", "Намылить руки", 1),
                SequenceStep(3, "💦", "Смыть мыло", 2),
                SequenceStep(4, "🧻", "Вытереть руки", 3)
            )
        ),
        SequenceLevel(
            "Одевание",
            "Разложи шаги одевания по порядку",
            listOf(
                SequenceStep(1, "👕", "Надеть футболку", 0),
                SequenceStep(2, "👖", "Надеть штаны", 1),
                SequenceStep(3, "🧦", "Надеть носки", 2),
                SequenceStep(4, "👟", "Надеть ботинки", 3)
            )
        ),
        SequenceLevel(
            "Завтрак",
            "Разложи шаги приготовления завтрака",
            listOf(
                SequenceStep(1, "🥚", "Взять яйцо", 0),
                SequenceStep(2, "🍳", "Разбить на сковороду", 1),
                SequenceStep(3, "🔥", "Поджарить", 2),
                SequenceStep(4, "🍽️", "Подать на тарелку", 3)
            )
        ),
        SequenceLevel(
            "Чистка зубов",
            "Разложи шаги чистки зубов по порядку",
            listOf(
                SequenceStep(1, "🪥", "Взять зубную щётку", 0),
                SequenceStep(2, "🧴", "Нанести пасту", 1),
                SequenceStep(3, "😁", "Чистить зубы", 2),
                SequenceStep(4, "💧", "Прополоскать рот", 3)
            )
        )
    )

    private val _levelIndex = MutableStateFlow(0)
    val levelIndex = _levelIndex.asStateFlow()

    private val _shuffledSteps = MutableStateFlow(levels[0].steps.shuffled())
    val shuffledSteps = _shuffledSteps.asStateFlow()

    private val _selectedOrder = MutableStateFlow<List<Int>>(emptyList())
    val selectedOrder = _selectedOrder.asStateFlow()

    private val _result = MutableStateFlow<Boolean?>(null)
    val result = _result.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    val currentLevel: SequenceLevel get() = levels[_levelIndex.value]
    val totalLevels: Int get() = levels.size

    fun toggleStep(stepId: Int) {
        if (_result.value != null) return
        val current = _selectedOrder.value.toMutableList()
        if (current.contains(stepId)) {
            current.remove(stepId)
        } else {
            current.add(stepId)
        }
        _selectedOrder.value = current
    }

    fun checkAnswer() {
        val selected = _selectedOrder.value
        if (selected.size < currentLevel.steps.size) return
        val correct = currentLevel.steps.sortedBy { it.correctPosition }.map { it.id }
        val isCorrect = selected == correct
        _result.value = isCorrect
        if (isCorrect) _score.value++
    }

    fun nextLevel() {
        val next = _levelIndex.value + 1
        if (next < levels.size) {
            _levelIndex.value = next
            _shuffledSteps.value = levels[next].steps.shuffled()
            _selectedOrder.value = emptyList()
            _result.value = null
        }
    }

    fun retryLevel() {
        _shuffledSteps.value = currentLevel.steps.shuffled()
        _selectedOrder.value = emptyList()
        _result.value = null
    }

    fun restart() {
        _levelIndex.value = 0
        _shuffledSteps.value = levels[0].steps.shuffled()
        _selectedOrder.value = emptyList()
        _result.value = null
        _score.value = 0
    }
}
