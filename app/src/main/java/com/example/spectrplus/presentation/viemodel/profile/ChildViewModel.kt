package com.example.spectrplus.presentation.viemodel.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.model.profile.Child
import com.example.spectrplus.domain.model.profile.CreateChildRequest
import com.example.spectrplus.domain.model.profile.TherapyRequest
import com.example.spectrplus.domain.model.profile.UpdateChildRequest
import com.example.spectrplus.domain.interactor.profile.ChildInteractor
import com.example.spectrplus.presentation.state.AddChildState
import com.example.spectrplus.presentation.state.TherapyInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildViewModel @Inject constructor(
    private val childInteractor: ChildInteractor
) : ViewModel() {

    var state by mutableStateOf(AddChildState())
        private set

    fun onNameChange(v: String) { state = state.copy(name = v) }
    fun onAgeChange(v: String) { state = state.copy(age = v) }
    fun onGenderChange(v: String) { state = state.copy(gender = v) }
    fun onDiagnosisChange(v: String) { state = state.copy(diagnosis = v) }
    fun onFeaturesChange(v: String) { state = state.copy(features = v) }
    fun onNotesChange(v: String) { state = state.copy(notes = v) }

    fun addTherapy() {
        state = state.copy(therapies = state.therapies + TherapyInput())
    }

    fun removeTherapy(index: Int) {
        state = state.copy(
            therapies = state.therapies.toMutableList().also {
                if (index in it.indices) it.removeAt(index)
            }
        )
    }

    fun onTherapyTitleChange(index: Int, v: String) {
        state = state.copy(
            therapies = state.therapies.mapIndexed { i, t ->
                if (i == index) t.copy(title = v) else t
            }
        )
    }

    fun onTherapyDescriptionChange(index: Int, v: String) {
        state = state.copy(
            therapies = state.therapies.mapIndexed { i, t ->
                if (i == index) t.copy(description = v) else t
            }
        )
    }

    fun reset() {
        state = AddChildState()
    }

    fun prefillForEdit(child: Child) {
        state = AddChildState(
            editingId = child.id,
            name = child.name,
            age = child.age.toString(),
            gender = child.gender,
            diagnosis = child.diagnosis,
            features = child.features,
            notes = child.notes,
            therapies = child.therapies.map {
                TherapyInput(title = it.title, description = it.description)
            }
        )
    }

    fun addChild(onSuccess: () -> Unit) {
        viewModelScope.launch {

            if (!validate()) return@launch

            state = state.copy(isLoading = true, error = null)

            try {
                childInteractor.addChild(
                    CreateChildRequest(
                        name = state.name,
                        age = state.age.toInt(),
                        gender = state.gender,
                        diagnosis = state.diagnosis,
                        features = state.features,
                        notes = state.notes,
                        therapies = state.therapies
                            .filter { it.title.isNotBlank() }
                            .map { TherapyRequest(it.title, it.description) }
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            }

            state = state.copy(isLoading = false)
        }
    }

    fun updateChild(onSuccess: () -> Unit) {
        val id = state.editingId ?: return
        viewModelScope.launch {

            if (!validate()) return@launch

            state = state.copy(isLoading = true, error = null)

            try {
                childInteractor.updateChild(
                    id,
                    UpdateChildRequest(
                        name = state.name,
                        age = state.age.toInt(),
                        gender = state.gender,
                        diagnosis = state.diagnosis,
                        features = state.features,
                        notes = state.notes,
                        therapies = state.therapies
                            .filter { it.title.isNotBlank() }
                            .map { TherapyRequest(it.title, it.description) }
                    )
                )
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            }

            state = state.copy(isLoading = false)
        }
    }

    fun deleteChild(id: Long, onSuccess: () -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null)
            try {
                childInteractor.deleteChild(id)
                onSuccess()
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            }
            state = state.copy(isLoading = false)
        }
    }

    private fun validate(): Boolean {
        if (state.name.isBlank() || state.age.isBlank()) {
            state = state.copy(error = "Заполните имя и возраст")
            return false
        }
        if (state.age.toIntOrNull() == null) {
            state = state.copy(error = "Возраст должен быть числом")
            return false
        }
        return true
    }
}
