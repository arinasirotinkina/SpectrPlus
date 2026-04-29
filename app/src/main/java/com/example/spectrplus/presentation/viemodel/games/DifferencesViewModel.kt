package com.example.spectrplus.presentation.viemodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class DifferenceLevel(
    val title: String,
    val leftItems: List<String>,
    val rightItems: List<String>,
    val differenceIndices: Set<Int>
)

@HiltViewModel
class DifferencesViewModel @Inject constructor() : ViewModel() {

    private val levels = listOf(
        DifferenceLevel(
            "В парке",
            listOf("🌞", "🌳", "🏠", "🌸", "🐱", "🚗", "🦋", "🌈"),
            listOf("🌝", "🌲", "🏠", "🌻", "🐱", "🚕", "🦋", "🌈"),
            setOf(0, 1, 3, 5)
        ),
        DifferenceLevel(
            "На кухне",
            listOf("🍎", "🥛", "🍞", "🥕", "☕", "🍳", "🥣", "🧃"),
            listOf("🍊", "🥛", "🧁", "🥕", "🍵", "🍳", "🍜", "🧃"),
            setOf(0, 2, 4, 6)
        ),
        DifferenceLevel(
            "В лесу",
            listOf("🌲", "🐻", "🍄", "🌿", "🦌", "🐦", "🌸", "🍁"),
            listOf("🌳", "🐻", "🍄", "🌿", "🦊", "🦅", "🌺", "🍁"),
            setOf(0, 4, 5, 6)
        ),
        DifferenceLevel(
            "На улице",
            listOf("🚌", "🏪", "🌤️", "👦", "🐕", "🚲", "🌳", "🏫"),
            listOf("🚎", "🏪", "⛅", "👧", "🐕", "🛴", "🌲", "🏫"),
            setOf(0, 2, 3, 5, 6)
        )
    )

    private val _levelIndex = MutableStateFlow(0)
    val levelIndex = _levelIndex.asStateFlow()

    private val _foundDifferences = MutableStateFlow<Set<Int>>(emptySet())
    val foundDifferences = _foundDifferences.asStateFlow()

    private val _wrongTaps = MutableStateFlow<Set<Int>>(emptySet())
    val wrongTaps = _wrongTaps.asStateFlow()

    private val _completed = MutableStateFlow(false)
    val completed = _completed.asStateFlow()

    val currentLevel: DifferenceLevel get() = levels[_levelIndex.value]
    val totalLevels: Int get() = levels.size

    fun tapDifference(index: Int) {
        if (_completed.value) return
        val found = _foundDifferences.value.toMutableSet()
        if (currentLevel.differenceIndices.contains(index)) {
            found.add(index)
            _foundDifferences.value = found
            if (found.size == currentLevel.differenceIndices.size) {
                _completed.value = true
            }
        } else {
            val wrong = _wrongTaps.value.toMutableSet()
            wrong.add(index)
            _wrongTaps.value = wrong
        }
    }

    fun clearWrongTap(index: Int) {
        val wrong = _wrongTaps.value.toMutableSet()
        wrong.remove(index)
        _wrongTaps.value = wrong
    }

    fun nextLevel() {
        val next = _levelIndex.value + 1
        if (next < levels.size) {
            _levelIndex.value = next
            _foundDifferences.value = emptySet()
            _wrongTaps.value = emptySet()
            _completed.value = false
        }
    }

    fun restart() {
        _levelIndex.value = 0
        _foundDifferences.value = emptySet()
        _wrongTaps.value = emptySet()
        _completed.value = false
    }
}
