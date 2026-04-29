package com.example.spectrplus.presentation.viewmodel.social

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.interactor.profile.UserInteractor
import com.example.spectrplus.domain.model.social.PublicUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userInteractor: UserInteractor
) : ViewModel() {

    var user by mutableStateOf<PublicUser?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun load(userId: Long) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                user = userInteractor.getPublicUser(userId)
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }
}
