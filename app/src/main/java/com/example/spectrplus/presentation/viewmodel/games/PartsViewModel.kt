package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PartOption(val id: String, val name: String, val emoji: String)

data class PartsPuzzle(
    val objectName: String,
    val question: String,
    val incompleteEmoji: String,
    val completeEmoji: String,
    val options: List<PartOption>,
    val correctOptionId: String
)

@HiltViewModel
class PartsViewModel @Inject constructor() : ViewModel() {

    private val puzzles = listOf(
        PartsPuzzle(
            "Машина", "Чего не хватает у машины?",
            "🚗💨", "🚗",
            listOf(
                PartOption("wheels", "Колёса", "🛞"),
                PartOption("music", "Музыка", "🎵"),
                PartOption("hat", "Шляпа", "🎩")
            ),
            "wheels"
        ),
        PartsPuzzle(
            "Лицо", "Чего не хватает на лице?",
            "😶", "😊",
            listOf(
                PartOption("eyes", "Глаза", "👁️"),
                PartOption("legs", "Ноги", "🦵"),
                PartOption("tail", "Хвост", "🐾")
            ),
            "eyes"
        ),
        PartsPuzzle(
            "Дерево", "Чего не хватает у дерева?",
            "🪵", "🌳",
            listOf(
                PartOption("roots", "Корни", "🌱"),
                PartOption("leaves", "Листья", "🍃"),
                PartOption("wings", "Крылья", "🪶")
            ),
            "leaves"
        ),
        PartsPuzzle(
            "Велосипед", "Чего не хватает у велосипеда?",
            "🛞➡️❓", "🚲",
            listOf(
                PartOption("pedals", "Педали", "⚙️"),
                PartOption("horn", "Рожок", "📯"),
                PartOption("hat", "Шляпа", "🎩")
            ),
            "pedals"
        ),
        PartsPuzzle(
            "Домик", "Чего не хватает у домика?",
            "🏠❓", "🏡",
            listOf(
                PartOption("door", "Дверь", "🚪"),
                PartOption("fish", "Рыба", "🐟"),
                PartOption("cloud", "Облако", "☁️")
            ),
            "door"
        ),
        PartsPuzzle(
            "Солнце", "Чего не хватает у солнца?",
            "⭕", "☀️",
            listOf(
                PartOption("rays", "Лучи", "✨"),
                PartOption("snow", "Снег", "❄️"),
                PartOption("rain", "Дождь", "🌧️")
            ),
            "rays"
        )
    )

    private val _puzzleIndex = MutableStateFlow(0)
    val puzzleIndex = _puzzleIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption = _selectedOption.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect = _isCorrect.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    val currentPuzzle: PartsPuzzle get() = puzzles[_puzzleIndex.value]
    val totalPuzzles: Int get() = puzzles.size

    fun selectOption(optionId: String) {
        if (_selectedOption.value != null) return
        _selectedOption.value = optionId
        val correct = optionId == currentPuzzle.correctOptionId
        _isCorrect.value = correct
        if (correct) _score.value++
    }

    fun next() {
        val next = _puzzleIndex.value + 1
        if (next >= puzzles.size) {
            _completed.value = true
        } else {
            _puzzleIndex.value = next
            _selectedOption.value = null
            _isCorrect.value = null
        }
    }

    fun restart() {
        _puzzleIndex.value = 0
        _selectedOption.value = null
        _isCorrect.value = null
        _score.value = 0
        _completed.value = false
    }
}
