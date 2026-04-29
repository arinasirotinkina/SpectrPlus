package com.example.spectrplus.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.presentation.viemodel.auth.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    asSpecialist: Boolean = false,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.registerState
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.resetRegisterState() }

    val password = state.password
    val hasDigit = password.any { it.isDigit() }
    val hasLower = password.any { it.isLowerCase() && it.code < 128 }
    val hasUpper = password.any { it.isUpperCase() && it.code < 128 }
    val hasLength = password.length >= 6

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpectrColors.Bg)
            .verticalScroll(rememberScrollState())
    ) {
        AuthHeader(
            title = if (asSpecialist) "Регистрация специалиста" else "Регистрация",
            subtitle = if (asSpecialist) {
                "Откройте доступ к аккаунту специалиста и помогайте"
            } else {
                "Приложение для помощи родителям детей с РАС"
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = viewModel::onRegisterFirstNameChange,
                    label = { Text("Имя") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = viewModel::onRegisterLastNameChange,
                    label = { Text("Фамилия") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = state.phone,
                onValueChange = viewModel::onRegisterPhoneChange,
                label = { Text("Телефон") },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = SpectrColors.TextMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onRegisterEmailChange,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = SpectrColors.TextMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            if (asSpecialist) {
                OutlinedTextField(
                    value = state.specialistProfession,
                    onValueChange = viewModel::onRegisterProfessionChange,
                    label = { Text("Профиль работы (психолог, врач, логопед…)") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onRegisterPasswordChange,
                label = { Text("Пароль") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = SpectrColors.TextMuted) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painterResource(if (passwordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                            null,
                            tint = SpectrColors.TextMuted
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            if (password.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SpectrColors.PrimarySoft, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PasswordRule("Минимум 6 символов", hasLength)
                    PasswordRule("Заглавная буква (A–Z)", hasUpper)
                    PasswordRule("Строчная буква (a–z)", hasLower)
                    PasswordRule("Цифра (0–9)", hasDigit)
                }
            }


            OutlinedTextField(
                value = state.repeatPassword,
                onValueChange = viewModel::onRegisterRepeatPasswordChange,
                label = { Text("Повторите пароль") },
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = SpectrColors.TextMuted) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painterResource(if (passwordVisible) R.drawable.baseline_visibility_off_24 else R.drawable.baseline_visibility_24),
                            null,
                            tint = SpectrColors.TextMuted
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            state.error?.let { ErrorBox(it) }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = { viewModel.register(navController, asSpecialist) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Зарегистрироваться", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Уже есть аккаунт?", color = SpectrColors.TextMuted, fontSize = 14.sp)
                TextButton(onClick = { navController.popBackStack() }) {
                    Text("Войти", color = SpectrColors.Primary, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PasswordRule(text: String, passed: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            if (passed) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (passed) SpectrColors.Success else SpectrColors.TextMuted,
            modifier = Modifier
                .size(18.dp)
                .background(
                    if (passed) SpectrColors.SuccessSoft else Color.Transparent,
                    CircleShape
                )
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text,
            fontSize = 13.sp,
            color = if (passed) SpectrColors.Success else SpectrColors.TextMuted
        )
    }
}
