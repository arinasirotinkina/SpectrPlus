package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class LogicItem(val id: String, val name: String, val emoji: String)

data class LogicSituation(
    val title: String,
    val emoji: String,
    val description: String,
    val allItems: List<LogicItem>,
    val neededIds: Set<String>
)

@HiltViewModel
class LogicViewModel @Inject constructor() : ViewModel() {

    private val situations = listOf(
        LogicSituation(
            "Чистка зубов", "🦷",
            "Что нужно для чистки зубов?",
            listOf(
                LogicItem("brush", "Зубная щётка", "🪥"),
                LogicItem("paste", "Зубная паста", "🧴"),
                LogicItem("water", "Вода", "💧"),
                LogicItem("pillow", "Подушка", "🛏️"),
                LogicItem("book", "Книга", "📚"),
                LogicItem("shoe", "Ботинок", "👟")
            ),
            setOf("brush", "paste", "water")
        ),
        LogicSituation(
            "Завтрак", "🥣",
            "Что нужно для завтрака?",
            listOf(
                LogicItem("bowl", "Тарелка", "🍽️"),
                LogicItem("spoon", "Ложка", "🥄"),
                LogicItem("food", "Еда", "🥣"),
                LogicItem("hammer", "Молоток", "🔨"),
                LogicItem("kite", "Воздушный змей", "🪁"),
                LogicItem("umbrella", "Зонт", "☂️")
            ),
            setOf("bowl", "spoon", "food")
        ),
        LogicSituation(
            "Прогулка под дождём", "🌧️",
            "Что взять на прогулку под дождём?",
            listOf(
                LogicItem("umbrella", "Зонт", "☂️"),
                LogicItem("boots", "Резиновые сапоги", "🥾"),
                LogicItem("coat", "Плащ", "🧥"),
                LogicItem("sunglasses", "Солнечные очки", "🕶️"),
                LogicItem("ball", "Мяч", "⚽"),
                LogicItem("flipflops", "Шлёпанцы", "🩴")
            ),
            setOf("umbrella", "boots", "coat")
        ),
        LogicSituation(
            "Рисование", "🎨",
            "Что нужно для рисования?",
            listOf(
                LogicItem("pencil", "Карандаш", "✏️"),
                LogicItem("paper", "Бумага", "📄"),
                LogicItem("paint", "Краски", "🎨"),
                LogicItem("pillow", "Подушка", "🛏️"),
                LogicItem("phone", "Телефон", "📱"),
                LogicItem("shoes", "Ботинки", "👟")
            ),
            setOf("pencil", "paper", "paint")
        ),
        LogicSituation(
            "Поход в магазин", "🛒",
            "Что нужно для похода в магазин?",
            listOf(
                LogicItem("bag", "Сумка", "👜"),
                LogicItem("money", "Деньги", "💵"),
                LogicItem("list", "Список покупок", "📝"),
                LogicItem("ski", "Лыжи", "🎿"),
                LogicItem("telescope", "Телескоп", "🔭"),
                LogicItem("drum", "Барабан", "🥁")
            ),
            setOf("bag", "money", "list")
        )
    )

    private val _situationIndex = MutableStateFlow(0)
    val situationIndex = _situationIndex.asStateFlow()

    private val _selectedItems = MutableStateFlow<Set<String>>(emptySet())
    val selectedItems = _selectedItems.asStateFlow()

    private val _checked = MutableStateFlow(false)
    val checked = _checked.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    val currentSituation: LogicSituation get() = situations[_situationIndex.value]
    val totalSituations: Int get() = situations.size

    fun toggleItem(itemId: String) {
        if (_checked.value) return
        val current = _selectedItems.value.toMutableSet()
        if (current.contains(itemId)) current.remove(itemId) else current.add(itemId)
        _selectedItems.value = current
    }

    fun check() {
        _checked.value = true
        if (_selectedItems.value == currentSituation.neededIds) {
            _score.value++
        }
    }

    fun next() {
        val next = _situationIndex.value + 1
        if (next >= situations.size) {
            _completed.value = true
        } else {
            _situationIndex.value = next
            _selectedItems.value = emptySet()
            _checked.value = false
        }
    }

    fun restart() {
        _situationIndex.value = 0
        _selectedItems.value = emptySet()
        _checked.value = false
        _score.value = 0
        _completed.value = false
    }
}
