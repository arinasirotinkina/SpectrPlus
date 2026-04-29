package com.example.spectrplus.presentation.viewmodel.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spectrplus.core.datastore.DataStoreManager
import com.example.spectrplus.core.network.parseApiError
import com.example.spectrplus.domain.interactor.auth.AuthInteractor
import com.example.spectrplus.presentation.state.AuthState
import com.example.spectrplus.presentation.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val dataStore: DataStoreManager
) : ViewModel() {

    var loginState by mutableStateOf(AuthState())
        private set

    var registerState by mutableStateOf(RegisterState())
        private set

    fun onLoginEmailChange(value: String) {
        loginState = loginState.copy(email = value)
    }

    fun onLoginPasswordChange(value: String) {
        loginState = loginState.copy(password = value)
    }

    fun login(navController: NavController) {
        viewModelScope.launch {
            loginState = loginState.copy(isLoading = true, error = null)

            try {
                val auth = authInteractor.login(loginState.email, loginState.password)
                dataStore.saveSession(auth.token, auth.accountRole)
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("AUTH", e.message ?: "error", e)
                loginState = loginState.copy(error = parseApiError(e))
            }

            loginState = loginState.copy(isLoading = false)
        }
    }

    fun onRegisterFirstNameChange(v: String) {
        registerState = registerState.copy(firstName = v)
    }
    fun onRegisterLastNameChange(v: String) {
        registerState = registerState.copy(lastName = v)
    }
    fun onRegisterEmailChange(v: String) {
        registerState = registerState.copy(email = v)
    }
    fun onRegisterPhoneChange(v: String) {
        registerState = registerState.copy(phone = v)
    }
    fun onRegisterPasswordChange(v: String) {
        registerState = registerState.copy(password = v)
    }
    fun onRegisterRepeatPasswordChange(v: String) {
        registerState = registerState.copy(repeatPassword = v)
    }

    fun onRegisterProfessionChange(v: String) {
        registerState = registerState.copy(specialistProfession = v)
    }

    fun resetRegisterState() {
        registerState = RegisterState()
    }

    fun register(navController: NavController, asSpecialist: Boolean = false) {
        val s = registerState

        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]+"
        if (!Pattern.matches(emailPattern, s.email)) {
            registerState = s.copy(error = "Некорректный email")
            return
        }
        val passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}"
        if (!Pattern.matches(passwordPattern, s.password)) {
            registerState = s.copy(
                error = "Пароль должен содержать: минимум 6 символов, 1 заглавная, 1 строчная, 1 цифра"
            )
            return
        }
        if (s.password != s.repeatPassword) {
            registerState = s.copy(error = "Пароли не совпадают")
            return
        }
        if (s.firstName.isBlank() || s.lastName.isBlank() || s.phone.isBlank()) {
            registerState = s.copy(error = "Заполните все поля")
            return
        }
        if (asSpecialist && s.specialistProfession.isBlank()) {
            registerState = s.copy(error = "Укажите профиль (например: психолог, врач)")
            return
        }

        viewModelScope.launch {
            registerState = registerState.copy(isLoading = true, error = null)
            try {
                val auth = authInteractor.register(
                    email = s.email,
                    phone = s.phone,
                    firstName = s.firstName,
                    lastName = s.lastName,
                    password = s.password,
                    asSpecialist = asSpecialist,
                    specialistProfession = s.specialistProfession.takeIf { asSpecialist }
                )
                dataStore.saveSession(auth.token, auth.accountRole)
                navController.navigate("main") {
                    popUpTo("login") { inclusive = true }
                }
            } catch (e: Exception) {
                Log.e("AUTH", e.message ?: "error", e)
                registerState = registerState.copy(error = parseApiError(e))
            }
            registerState = registerState.copy(isLoading = false)
        }
    }
}
