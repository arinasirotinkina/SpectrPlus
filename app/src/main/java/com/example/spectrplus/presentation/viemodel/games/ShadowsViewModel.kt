package com.example.spectrplus.presentation.viemodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ShadowItem(val id: Int, val name: String, val emoji: String)

@HiltViewModel
class ShadowsViewModel @Inject constructor() : ViewModel() {

    private val allItems = listOf(
        ShadowItem(1, "Кошка", "🐱"),
        ShadowItem(2, "Собака", "🐶"),
        ShadowItem(3, "Зайчик", "🐰"),
        ShadowItem(4, "Птица", "🐦"),
        ShadowItem(5, "Рыбка", "🐟"),
        ShadowItem(6, "Бабочка", "🦋"),
        ShadowItem(7, "Черепаха", "🐢"),
        ShadowItem(8, "Лягушка", "🐸")
    )

    private val _items = MutableStateFlow(allItems.shuffled())
    val items = _items.asStateFlow()

    private val _shadows = MutableStateFlow(allItems.shuffled())
    val shadows = _shadows.asStateFlow()

    private val _selectedItemId = MutableStateFlow<Int?>(null)
    val selectedItemId = _selectedItemId.asStateFlow()

    private val _matched = MutableStateFlow<Set<Int>>(emptySet())
    val matched = _matched.asStateFlow()

    private val _wrongPair = MutableStateFlow<Pair<Int, Int>?>(null)
    val wrongPair = _wrongPair.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    fun selectItem(id: Int) {
        _selectedItemId.value = if (_selectedItemId.value == id) null else id
    }

    fun matchShadow(shadowId: Int) {
        val selectedId = _selectedItemId.value ?: return
        if (selectedId == shadowId) {
            val newMatched = _matched.value.toMutableSet()
            newMatched.add(shadowId)
            _matched.value = newMatched
            _selectedItemId.value = null
            if (newMatched.size == allItems.size) {
                _completed.value = true
            }
        } else {
            _wrongPair.value = Pair(selectedId, shadowId)
            _selectedItemId.value = null
        }
    }

    fun clearWrongPair() {
        _wrongPair.value = null
    }

    fun restart() {
        _items.value = allItems.shuffled()
        _shadows.value = allItems.shuffled()
        _selectedItemId.value = null
        _matched.value = emptySet()
        _wrongPair.value = null
        _completed.value = false
    }
}
