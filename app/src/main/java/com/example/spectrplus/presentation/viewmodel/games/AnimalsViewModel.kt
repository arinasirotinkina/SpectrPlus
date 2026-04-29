package com.example.spectrplus.presentation.viewmodel.games

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AnimalData(
    val id: Int,
    val name: String,
    val emoji: String,
    val sound: String,
    val speechText: String,
    val bgColor: Long
)

@HiltViewModel
class AnimalsViewModel @Inject constructor() : ViewModel() {

    val animals = listOf(
        AnimalData(1, "Собака", "🐕", "Гав-гав!", "Собака говорит гав гав", 0xFFFFECB3),
        AnimalData(2, "Кошка", "🐈", "Мяу!", "Кошка говорит мяу", 0xFFFCE4EC),
        AnimalData(3, "Корова", "🐄", "Му-у!", "Корова говорит му", 0xFFE8F5E9),
        AnimalData(4, "Лошадь", "🐴", "И-го-го!", "Лошадь говорит и го го", 0xFFE3F2FD),
        AnimalData(5, "Свинья", "🐷", "Хрю-хрю!", "Свинья говорит хрю хрю", 0xFFFFF9C4),
        AnimalData(6, "Овца", "🐑", "Бе-е!", "Овца говорит бе", 0xFFEDE7F6),
        AnimalData(7, "Курица", "🐔", "Ко-ко!", "Курица говорит ко ко", 0xFFFFECB3),
        AnimalData(8, "Лягушка", "🐸", "Ква-ква!", "Лягушка говорит ква ква", 0xFFE8F5E9),
        AnimalData(9, "Утка", "🦆", "Кря-кря!", "Утка говорит кря кря", 0xFFE3F2FD),
        AnimalData(10, "Пчела", "🐝", "Жжж!", "Пчела жужжит", 0xFFFFF9C4),
        AnimalData(11, "Лев", "🦁", "Р-р-р!", "Лев рычит", 0xFFFFE0B2),
        AnimalData(12, "Медведь", "🐻", "У-у-у!", "Медведь рычит у у у", 0xFFEFEBE9)
    )

    private val _activeAnimalId = MutableStateFlow<Int?>(null)
    val activeAnimalId = _activeAnimalId.asStateFlow()

    fun onAnimalTapped(id: Int) {
        _activeAnimalId.value = if (_activeAnimalId.value == id) null else id
    }

    fun getAnimal(id: Int) = animals.find { it.id == id }
}
