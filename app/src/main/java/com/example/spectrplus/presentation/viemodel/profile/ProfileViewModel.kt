package com.example.spectrplus.presentation.viemodel.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spectrplus.core.datastore.DataStoreManager
import com.example.spectrplus.domain.model.profile.UpdateProfileRequest
import com.example.spectrplus.domain.interactor.profile.ProfileInteractor
import com.example.spectrplus.presentation.state.EditProfileState
import com.example.spectrplus.presentation.state.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileInteractor: ProfileInteractor,
    private val dataStore: DataStoreManager
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    var editState by mutableStateOf(EditProfileState())
        private set

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val profile = profileInteractor.getProfile()
                state = state.copy(profile = profile)
                dataStore.saveAccountRole(profile.accountRole)

                editState = editState.copy(
                    firstName = profile.firstName,
                    lastName = profile.lastName,
                    phone = profile.phone,
                    city = profile.city ?: "",
                    showChildInPublicProfile = profile.showChildInPublicProfile,
                    specialistProfession = profile.specialistProfession ?: "",
                    specialistEducation = profile.specialistEducation ?: "",
                    specialistExperienceYears = profile.specialistExperienceYears?.toString() ?: ""
                )
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            }

            state = state.copy(isLoading = false)
        }
    }

    fun onFirstNameChange(v: String) {
        editState = editState.copy(firstName = v)
    }

    fun onLastNameChange(v: String) {
        editState = editState.copy(lastName = v)
    }

    fun onPhoneChange(v: String) {
        editState = editState.copy(phone = v)
    }

    fun onCityChange(v: String) {
        editState = editState.copy(city = v)
    }

    fun onShowChildInPublicProfileChange(v: Boolean) {
        editState = editState.copy(showChildInPublicProfile = v)
    }

    fun onSpecialistProfessionChange(v: String) {
        editState = editState.copy(specialistProfession = v)
    }

    fun onSpecialistEducationChange(v: String) {
        editState = editState.copy(specialistEducation = v)
    }

    fun onSpecialistExperienceYearsChange(v: String) {
        editState = editState.copy(specialistExperienceYears = v.filter { it.isDigit() }.take(3))
    }

    fun saveProfile(navController: NavController) {
        viewModelScope.launch {

            if (!validate()) return@launch

            editState = editState.copy(isLoading = true)

            try {
                val profile = state.profile
                val isSpecialist = profile?.accountRole == "SPECIALIST"
                profileInteractor.updateProfile(
                    UpdateProfileRequest(
                        firstName = editState.firstName,
                        lastName = editState.lastName,
                        phone = editState.phone,
                        city = editState.city.takeIf { it.isNotBlank() },
                        showChildInPublicProfile = if (!isSpecialist) editState.showChildInPublicProfile else false,
                        specialistProfession = if (isSpecialist) {
                            editState.specialistProfession.takeIf { it.isNotBlank() }
                        } else null,
                        specialistEducation = if (isSpecialist) {
                            editState.specialistEducation.takeIf { it.isNotBlank() }
                        } else null,
                        specialistExperienceYears = if (isSpecialist) {
                            editState.specialistExperienceYears.toIntOrNull()
                        } else null
                    )
                )

                loadProfile()

                navController.popBackStack()
            } catch (e: Exception) {
                editState = editState.copy(error = e.message)
            }

            editState = editState.copy(isLoading = false)
        }
    }

    private fun validate(): Boolean {

        if (editState.firstName.isBlank() ||
            editState.lastName.isBlank() ||
            editState.phone.isBlank()
        ) {
            editState = editState.copy(error = "Заполните обязательные поля")
            return false
        }

        editState = editState.copy(error = null)
        return true
    }

    fun logout() {
        viewModelScope.launch {
            dataStore.clear()
        }
    }

    fun uploadAvatar(context: Context, uri: Uri) {
        viewModelScope.launch {
            editState = editState.copy(isLoading = true, error = null)
            try {
                val resolver = context.contentResolver
                val mime = resolver.getType(uri) ?: "image/jpeg"
                val ext = when {
                    mime.contains("png") -> "png"
                    mime.contains("webp") -> "webp"
                    else -> "jpg"
                }
                val name = "avatar_${System.currentTimeMillis()}.$ext"
                val input = resolver.openInputStream(uri)
                    ?: throw RuntimeException("Не удалось прочитать файл")
                profileInteractor.uploadAvatar(name, mime, input)
                loadProfile()
            } catch (e: Exception) {
                editState = editState.copy(error = e.message)
            }
            editState = editState.copy(isLoading = false)
        }
    }

    fun removeAvatar() {
        viewModelScope.launch {
            editState = editState.copy(isLoading = true, error = null)
            try {
                profileInteractor.deleteAvatar()
                loadProfile()
            } catch (e: Exception) {
                editState = editState.copy(error = e.message)
            }
            editState = editState.copy(isLoading = false)
        }
    }
}
