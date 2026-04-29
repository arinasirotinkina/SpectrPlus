package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class OddOneRound(
    val options: List<String>,
    val oddIndex: Int,
    val hint: String
)

@HiltViewModel
class OddOneOutViewModel @Inject constructor() : ViewModel() {

    private val rounds = listOf(
        OddOneRound(listOf("🍎", "🍐", "🍊", "🐕"), 3, "Три фрукта и одно животное"),
        OddOneRound(listOf("🐱", "🐱", "🐱", "🐶"), 3, "Три кошки и одна собака"),
        OddOneRound(listOf("⚽", "🏀", "🏐", "🍰"), 3, "Три мяча и не мяч"),
        OddOneRound(listOf("🚗", "🚌", "🚕", "🍌"), 3, "Транспорт и фрукт"),
        OddOneRound(listOf("🌞", "☀️", "🌤️", "🌙"), 3, "День и ночь"),
        OddOneRound(listOf("🥕", "🥦", "🌽", "🍦"), 3, "Овощи и сладкое"),
        OddOneRound(listOf("🔴", "🟥", "❤️", "🟦"), 3, "Красное и синее"),
        OddOneRound(listOf("🐟", "🐠", "🐡", "🐄"), 3, "Рыбы и не рыба")
    )

    val totalRounds: Int get() = rounds.size

    private val _roundIndex = MutableStateFlow(0)
    val roundIndex = _roundIndex.asStateFlow()

    private val _wrongPick = MutableStateFlow<Int?>(null)
    val wrongPick = _wrongPick.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    val currentRound: OddOneRound get() = rounds[_roundIndex.value.coerceIn(0, rounds.lastIndex)]

    fun onPick(index: Int) {
        val r = currentRound
        if (index == r.oddIndex) {
            _wrongPick.value = null
            if (_roundIndex.value >= rounds.lastIndex) {
                _completed.value = true
            } else {
                _roundIndex.value = _roundIndex.value + 1
            }
        } else {
            _wrongPick.value = index
        }
    }

    fun clearWrong() {
        _wrongPick.value = null
    }

    fun restart() {
        _roundIndex.value = 0
        _wrongPick.value = null
        _completed.value = false
    }
}
