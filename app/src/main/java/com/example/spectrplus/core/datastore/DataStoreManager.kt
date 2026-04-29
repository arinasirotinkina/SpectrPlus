package com.example.spectrplus.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("auth")

    private val TOKEN_KEY = stringPreferencesKey("token")
    private val ACCOUNT_ROLE_KEY = stringPreferencesKey("account_role")

    val tokenFlow: Flow<String?> = context.dataStore.data
        .map { prefs ->
            prefs[TOKEN_KEY]
        }

    val accountRoleFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[ACCOUNT_ROLE_KEY] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun saveSession(token: String, accountRole: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[ACCOUNT_ROLE_KEY] = accountRole
        }
    }

    suspend fun saveAccountRole(accountRole: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCOUNT_ROLE_KEY] = accountRole
        }
    }

    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}