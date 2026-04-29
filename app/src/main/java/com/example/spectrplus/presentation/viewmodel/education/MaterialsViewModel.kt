package com.example.spectrplus.presentation.viewmodel.education

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.education.Material
import com.example.spectrplus.domain.interactor.education.MaterialInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsViewModel @Inject constructor(
    private val materialInteractor: MaterialInteractor
) : ViewModel() {

    var state by mutableStateOf(MaterialsState())
        private set

    fun load(category: String? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val materials = materialInteractor.getMaterials(category)
                state = state.copy(
                    materials = materials,
                    isLoading = false
                )
                Log.i("materials", materials.toString())
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
                Log.i("materials", e.toString())
            }
        }
    }
}

data class MaterialsState(
    val materials: List<Material> = emptyList(),
    val isLoading: Boolean = false
)