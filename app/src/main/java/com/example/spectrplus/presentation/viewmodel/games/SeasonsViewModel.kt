package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import com.example.spectrplus.domain.model.games.Basket
import com.example.spectrplus.domain.model.games.SortItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SeasonsViewModel @Inject constructor() : ViewModel() {

    private val allItems = listOf(
        SortItem(1, "Шуба", "winter", "🧥", 0xFFBBDEFB),
        SortItem(2, "Купальник", "summer", "👙", 0xFFFFECB3),
        SortItem(3, "Санки", "winter", "🛷", 0xFFBBDEFB),
        SortItem(4, "Зонт", "autumn", "☂️", 0xFFE8EAF6),
        SortItem(5, "Шапка", "winter", "🧢", 0xFFBBDEFB),
        SortItem(6, "Мороженое", "summer", "🍦", 0xFFFFECB3),
        SortItem(7, "Листья", "autumn", "🍂", 0xFFE8EAF6),
        SortItem(8, "Цветы", "spring", "🌸", 0xFFFCE4EC),
        SortItem(9, "Солнечные очки", "summer", "🕶️", 0xFFFFECB3),
        SortItem(10, "Резиновые сапоги", "autumn", "🥾", 0xFFE8EAF6),
        SortItem(11, "Тюльпаны", "spring", "🌷", 0xFFFCE4EC),
        SortItem(12, "Снеговик", "winter", "⛄", 0xFFBBDEFB),
        SortItem(13, "Дождевик", "autumn", "🧥", 0xFFE8EAF6),
        SortItem(14, "Бабочка", "spring", "🦋", 0xFFFCE4EC),
        SortItem(15, "Велосипед", "summer", "🚲", 0xFFFFECB3),
        SortItem(16, "Подснежник", "spring", "❄️🌱", 0xFFFCE4EC)
    )

    val baskets = listOf(
        Basket("spring", "🌸 Весна", "spring"),
        Basket("summer", "☀️ Лето", "summer"),
        Basket("autumn", "🍂 Осень", "autumn"),
        Basket("winter", "⛄ Зима", "winter")
    )

    private val _items = MutableStateFlow(allItems.shuffled())
    val items = _items.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    private val _selectedItem = MutableStateFlow<SortItem?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    private val _wrongBasket = MutableStateFlow<String?>(null)
    val wrongBasket = _wrongBasket.asStateFlow()

    fun selectItem(item: SortItem) {
        _selectedItem.value = if (_selectedItem.value?.id == item.id) null else item
    }

    fun placeInBasket(basket: Basket) {
        val item = _selectedItem.value ?: return
        if (basket.accepts == item.category) {
            _items.value = _items.value.filterNot { it.id == item.id }
            _score.value++
            _selectedItem.value = null
            if (_items.value.isEmpty()) _completed.value = true
        } else {
            _wrongBasket.value = basket.id
            _selectedItem.value = null
        }
    }

    fun clearWrongBasket() {
        _wrongBasket.value = null
    }

    fun restart() {
        _items.value = allItems.shuffled()
        _score.value = 0
        _completed.value = false
        _selectedItem.value = null
        _wrongBasket.value = null
    }
}
