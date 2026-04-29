package com.example.spectrplus.presentation.viemodel.games

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.example.spectrplus.domain.model.games.Basket
import com.example.spectrplus.domain.model.games.SortItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class SortMode(val label: String) {
    CATEGORY("По категории"),
    COLOR("По цвету"),
    SEASON("По размеру")
}

@HiltViewModel
class SortingViewModel @Inject constructor() : ViewModel() {

    private val categoryLevels = listOf(
        Pair(
            listOf(
                SortItem(1, "Яблоко", "food", "🍎", 0xFFFFEBEE),
                SortItem(2, "Банан", "food", "🍌", 0xFFFFF9C4),
                SortItem(3, "Машина", "transport", "🚗", 0xFFE3F2FD),
                SortItem(4, "Самолёт", "transport", "✈️", 0xFFE3F2FD),
                SortItem(5, "Апельсин", "food", "🍊", 0xFFFFEBEE),
                SortItem(6, "Автобус", "transport", "🚌", 0xFFE3F2FD),
                SortItem(7, "Груша", "food", "🍐", 0xFFE8F5E9),
                SortItem(8, "Велосипед", "transport", "🚲", 0xFFE3F2FD),
            ),
            listOf(
                Basket("food", "🍽️ Еда", "food"),
                Basket("transport", "🚦 Транспорт", "transport")
            )
        ),
        Pair(
            listOf(
                SortItem(1, "Кошка", "animals", "🐱", 0xFFFCE4EC),
                SortItem(2, "Роза", "plants", "🌹", 0xFFE8F5E9),
                SortItem(3, "Собака", "animals", "🐶", 0xFFFCE4EC),
                SortItem(4, "Дерево", "plants", "🌳", 0xFFE8F5E9),
                SortItem(5, "Кролик", "animals", "🐰", 0xFFFCE4EC),
                SortItem(6, "Цветок", "plants", "🌷", 0xFFE8F5E9),
                SortItem(7, "Птица", "animals", "🐦", 0xFFFCE4EC),
                SortItem(8, "Кактус", "plants", "🌵", 0xFFE8F5E9),
            ),
            listOf(
                Basket("animals", "🐾 Животные", "animals"),
                Basket("plants", "🌿 Растения", "plants")
            )
        ),
        Pair(
            listOf(
                SortItem(1, "Футбол", "sport", "⚽", 0xFFE3F2FD),
                SortItem(2, "Книга", "learning", "📚", 0xFFFFF9C4),
                SortItem(3, "Баскетбол", "sport", "🏀", 0xFFE3F2FD),
                SortItem(4, "Карандаш", "learning", "✏️", 0xFFFFF9C4),
                SortItem(5, "Теннис", "sport", "🎾", 0xFFE3F2FD),
                SortItem(6, "Тетрадь", "learning", "📓", 0xFFFFF9C4),
                SortItem(7, "Плавание", "sport", "🏊", 0xFFE3F2FD),
                SortItem(8, "Глобус", "learning", "🌍", 0xFFFFF9C4),
            ),
            listOf(
                Basket("sport", "🏅 Спорт", "sport"),
                Basket("learning", "🎓 Учёба", "learning")
            )
        )
    )

    private val _currentLevel = MutableStateFlow(0)
    val currentLevel = _currentLevel.asStateFlow()

    private val _items = MutableStateFlow(categoryLevels[0].first.shuffled())
    val items = _items.asStateFlow()

    private val _baskets = MutableStateFlow(categoryLevels[0].second)
    val baskets = _baskets.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    private val _wrongAttempt = MutableStateFlow<Int?>(null)
    val wrongAttempt = _wrongAttempt.asStateFlow()

    fun removeItem(item: SortItem): Boolean {
        _items.value = _items.value.filterNot { it.id == item.id }
        _score.value++
        if (_items.value.isEmpty()) {
            _completed.value = true
        }
        return true
    }

    fun onWrongDrop(itemId: Int) {
        _wrongAttempt.value = itemId
    }

    fun clearWrongAttempt() {
        _wrongAttempt.value = null
    }

    fun nextLevel() {
        val next = _currentLevel.value + 1
        if (next < categoryLevels.size) {
            _currentLevel.value = next
            _items.value = categoryLevels[next].first.shuffled()
            _baskets.value = categoryLevels[next].second
            _completed.value = false
        }
    }

    fun restart() {
        _currentLevel.value = 0
        _items.value = categoryLevels[0].first.shuffled()
        _baskets.value = categoryLevels[0].second
        _score.value = 0
        _completed.value = false
    }

    val totalLevels: Int get() = categoryLevels.size
}

data class DragState(
    val item: SortItem? = null,
    val position: Offset = Offset.Zero,
    val touchOffset: Offset = Offset.Zero
)
