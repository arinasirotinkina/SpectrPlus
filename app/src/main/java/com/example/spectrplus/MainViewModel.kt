package com.example.spectrplus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.core.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class AuthStatus {
    data object Loading : AuthStatus()
    data object Unauthenticated : AuthStatus()
    data class Authenticated(val token: String) : AuthStatus()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStoreManager
) : ViewModel() {

    val authStatus: StateFlow<AuthStatus> = dataStore.tokenFlow
        .map { token ->
            if (token.isNullOrBlank()) AuthStatus.Unauthenticated
            else AuthStatus.Authenticated(token)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AuthStatus.Loading
        )
}
