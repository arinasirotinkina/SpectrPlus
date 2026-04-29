package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class WholePart(val id: Int, val emoji: String, val name: String)

data class WholePuzzle(
    val id: Int,
    val targetEmoji: String,
    val targetName: String,
    val description: String,
    val missingPart: WholePart,
    val options: List<WholePart>,
    val parts: List<WholePart>
)

data class PartsWholeState(
    val puzzles: List<WholePuzzle>,
    val currentIndex: Int = 0,
    val selectedOption: WholePart? = null,
    val isAnswered: Boolean = false,
    val isCorrect: Boolean = false,
    val score: Int = 0,
    val isComplete: Boolean = false
)

@HiltViewModel
class PartsWholeViewModel @Inject constructor() : ViewModel() {

    private val allPuzzles = listOf(
        WholePuzzle(
            id = 1,
            targetEmoji = "🐱",
            targetName = "Кошка",
            description = "Кошке не хватает...",
            missingPart = WholePart(1, "🐾", "Лапы"),
            options = listOf(
                WholePart(1, "🐾", "Лапы"),
                WholePart(2, "🦷", "Зубы"),
                WholePart(3, "🌊", "Вода"),
                WholePart(4, "🌲", "Дерево")
            ),
            parts = listOf(
                WholePart(10, "😺", "Голова"),
                WholePart(11, "🫀", "Тело"),
                WholePart(12, "🐈", "Хвост"),
                WholePart(13, "❓", "? Лапы")
            )
        ),
        WholePuzzle(
            id = 2,
            targetEmoji = "🚗",
            targetName = "Машина",
            description = "У машины нет...",
            missingPart = WholePart(1, "🔵", "Колёса"),
            options = listOf(
                WholePart(1, "🔵", "Колёса"),
                WholePart(2, "🌿", "Трава"),
                WholePart(3, "🍕", "Пицца"),
                WholePart(4, "🎵", "Музыка")
            ),
            parts = listOf(
                WholePart(10, "🟦", "Кузов"),
                WholePart(11, "🪟", "Окно"),
                WholePart(12, "💡", "Фара"),
                WholePart(13, "❓", "? Колёса")
            )
        ),
        WholePuzzle(
            id = 3,
            targetEmoji = "🏠",
            targetName = "Дом",
            description = "У дома не хватает...",
            missingPart = WholePart(1, "🔺", "Крыша"),
            options = listOf(
                WholePart(1, "🔺", "Крыша"),
                WholePart(2, "🐟", "Рыба"),
                WholePart(3, "🎈", "Шарик"),
                WholePart(4, "🍦", "Мороженое")
            ),
            parts = listOf(
                WholePart(10, "🟧", "Стены"),
                WholePart(11, "🚪", "Дверь"),
                WholePart(12, "🪟", "Окно"),
                WholePart(13, "❓", "? Крыша")
            )
        ),
        WholePuzzle(
            id = 4,
            targetEmoji = "🦋",
            targetName = "Бабочка",
            description = "У бабочки нет...",
            missingPart = WholePart(1, "🪶", "Крылья"),
            options = listOf(
                WholePart(1, "🪶", "Крылья"),
                WholePart(2, "🔑", "Ключ"),
                WholePart(3, "🧊", "Лёд"),
                WholePart(4, "📱", "Телефон")
            ),
            parts = listOf(
                WholePart(10, "🦎", "Тело"),
                WholePart(11, "🦁", "Голова"),
                WholePart(12, "〰️", "Усики"),
                WholePart(13, "❓", "? Крылья")
            )
        ),
        WholePuzzle(
            id = 5,
            targetEmoji = "🌻",
            targetName = "Подсолнух",
            description = "У цветка нет...",
            missingPart = WholePart(1, "🌿", "Листья"),
            options = listOf(
                WholePart(1, "🌿", "Листья"),
                WholePart(2, "🚀", "Ракета"),
                WholePart(3, "🎭", "Маска"),
                WholePart(4, "🛸", "НЛО")
            ),
            parts = listOf(
                WholePart(10, "🌼", "Цветок"),
                WholePart(11, "🟤", "Стебель"),
                WholePart(12, "🌱", "Корень"),
                WholePart(13, "❓", "? Листья")
            )
        )
    )

    private val _state = MutableStateFlow(PartsWholeState(puzzles = allPuzzles))
    val state = _state.asStateFlow()

    fun selectOption(part: WholePart) {
        if (_state.value.isAnswered) return
        val current = _state.value
        val correct = part.id == current.puzzles[current.currentIndex].missingPart.id
        _state.update {
            it.copy(
                selectedOption = part,
                isAnswered = true,
                isCorrect = correct,
                score = if (correct) it.score + 10 else it.score
            )
        }
    }

    fun nextPuzzle() {
        val current = _state.value
        val next = current.currentIndex + 1
        if (next >= current.puzzles.size) {
            _state.update { it.copy(isComplete = true) }
        } else {
            _state.update {
                it.copy(
                    currentIndex = next,
                    selectedOption = null,
                    isAnswered = false,
                    isCorrect = false
                )
            }
        }
    }

    fun restart() {
        _state.value = PartsWholeState(puzzles = allPuzzles)
    }
}
