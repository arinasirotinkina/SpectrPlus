package com.example.spectrplus.presentation.state

data class TherapyInput(
    val title: String = "",
    val description: String = ""
)

data class AddChildState(
    val editingId: Long? = null,
    val name: String = "",
    val age: String = "",
    val gender: String = "",
    val diagnosis: String = "",
    val features: String = "",
    val notes: String = "",
    val therapies: List<TherapyInput> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
