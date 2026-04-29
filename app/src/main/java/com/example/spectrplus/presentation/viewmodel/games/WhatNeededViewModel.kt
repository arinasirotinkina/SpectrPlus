package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class NeedItem(val id: Int, val emoji: String, val name: String, val isNeeded: Boolean)

data class NeedSituation(
    val title: String,
    val emoji: String,
    val hint: String,
    val items: List<NeedItem>
)

data class WhatNeededState(
    val situations: List<NeedSituation>,
    val currentIndex: Int = 0,
    val selected: Set<Int> = emptySet(),
    val isChecked: Boolean = false,
    val correctCount: Int = 0,
    val score: Int = 0,
    val isComplete: Boolean = false
)

@HiltViewModel
class WhatNeededViewModel @Inject constructor() : ViewModel() {

    private val allSituations = listOf(
        NeedSituation(
            title = "Чистка зубов",
            emoji = "🦷",
            hint = "Что нужно, чтобы почистить зубы?",
            items = listOf(
                NeedItem(1, "🪥", "Зубная щётка", true),
                NeedItem(2, "🧴", "Зубная паста", true),
                NeedItem(3, "💧", "Вода", true),
                NeedItem(4, "🍕", "Пицца", false),
                NeedItem(5, "📚", "Книга", false),
                NeedItem(6, "🚗", "Машина", false)
            )
        ),
        NeedSituation(
            title = "Прогулка под дождём",
            emoji = "🌧️",
            hint = "Что взять на прогулку в дождь?",
            items = listOf(
                NeedItem(1, "☂️", "Зонт", true),
                NeedItem(2, "🥾", "Резиновые сапоги", true),
                NeedItem(3, "🧥", "Дождевик", true),
                NeedItem(4, "🏖️", "Пляжный мяч", false),
                NeedItem(5, "🎮", "Игровой планшет", false),
                NeedItem(6, "🌵", "Кактус", false)
            )
        ),
        NeedSituation(
            title = "Завтрак",
            emoji = "🌅",
            hint = "Что нужно для завтрака?",
            items = listOf(
                NeedItem(1, "🥣", "Тарелка", true),
                NeedItem(2, "🥄", "Ложка", true),
                NeedItem(3, "🥛", "Молоко", true),
                NeedItem(4, "⛄", "Снеговик", false),
                NeedItem(5, "🎸", "Гитара", false),
                NeedItem(6, "🔧", "Гаечный ключ", false)
            )
        ),
        NeedSituation(
            title = "Рисование",
            emoji = "🎨",
            hint = "Что нужно, чтобы рисовать?",
            items = listOf(
                NeedItem(1, "🖌️", "Кисточка", true),
                NeedItem(2, "🎨", "Краски", true),
                NeedItem(3, "📄", "Бумага", true),
                NeedItem(4, "🍦", "Мороженое", false),
                NeedItem(5, "🚀", "Ракета", false),
                NeedItem(6, "🦷", "Зубная щётка", false)
            )
        ),
        NeedSituation(
            title = "Поход в спортзал",
            emoji = "🏋️",
            hint = "Что взять на спорт?",
            items = listOf(
                NeedItem(1, "👟", "Кроссовки", true),
                NeedItem(2, "🩳", "Спортивные шорты", true),
                NeedItem(3, "💧", "Бутылка воды", true),
                NeedItem(4, "🍰", "Торт", false),
                NeedItem(5, "🌊", "Волна", false),
                NeedItem(6, "🎭", "Маска", false)
            )
        )
    )

    private val _state = MutableStateFlow(WhatNeededState(situations = allSituations))
    val state = _state.asStateFlow()

    fun toggleItem(itemId: Int) {
        if (_state.value.isChecked) return
        _state.update { state ->
            val newSelected = if (state.selected.contains(itemId)) {
                state.selected - itemId
            } else {
                state.selected + itemId
            }
            state.copy(selected = newSelected)
        }
    }

    fun checkAnswer() {
        val current = _state.value
        val situation = current.situations[current.currentIndex]
        val needed = situation.items.filter { it.isNeeded }.map { it.id }.toSet()
        val correct = needed.intersect(current.selected).size
        _state.update {
            it.copy(
                isChecked = true,
                correctCount = correct,
                score = it.score + correct * 5
            )
        }
    }

    fun nextSituation() {
        val next = _state.value.currentIndex + 1
        if (next >= _state.value.situations.size) {
            _state.update { it.copy(isComplete = true) }
        } else {
            _state.update {
                it.copy(
                    currentIndex = next,
                    selected = emptySet(),
                    isChecked = false,
                    correctCount = 0
                )
            }
        }
    }

    fun restart() {
        _state.value = WhatNeededState(situations = allSituations)
    }
}
