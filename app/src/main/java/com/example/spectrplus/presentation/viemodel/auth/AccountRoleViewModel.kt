package com.example.spectrplus.presentation.viemodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.core.datastore.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AccountRoleViewModel @Inject constructor(
    dataStore: DataStoreManager
) : ViewModel() {

    val accountRole: StateFlow<String> = dataStore.accountRoleFlow
        .map { it ?: "PARENT" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "PARENT")
}
