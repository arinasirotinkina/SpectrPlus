package com.example.spectrplus.presentation.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state = viewModel.loginState
    val scope = rememberCoroutineScope()
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpectrColors.Bg)
            .verticalScroll(rememberScrollState())
    ) {
        AuthHeader(
            title = "Вход",
            subtitle = "Войдите, чтобы продолжить заботу о ребенке"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onLoginEmailChange,
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = SpectrColors.TextMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = spectrFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onLoginPasswordChange,
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

            state.error?.let {
                ErrorBox(it)
            }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = { scope.launch { viewModel.login(navController) } },
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
                    Text("Войти", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Нет аккаунта?",
                    color = SpectrColors.TextMuted,
                    fontSize = 14.sp
                )
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        "Зарегистрироваться",
                        color = SpectrColors.Primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            TextButton(
                onClick = { navController.navigate("register_specialist") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Регистрация для специалистов",
                    color = SpectrColors.Primary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
internal fun AuthHeader(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpectrColors.HeaderGradient)
            .padding(horizontal = 24.dp)
            .padding(top = 56.dp, bottom = 40.dp)
    ) {
        Column {
            Text("СпектрПлюс", color = Color.White.copy(alpha = 0.85f), fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                subtitle,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
internal fun ErrorBox(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(SpectrColors.DangerSoft, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Text(text, color = SpectrColors.Danger, fontSize = 13.sp)
    }
}
