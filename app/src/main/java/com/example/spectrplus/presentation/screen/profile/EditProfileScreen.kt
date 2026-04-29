package com.example.spectrplus.presentation.screen.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.InitialsAvatar
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.presentation.screen.auth.ErrorBox
import com.example.spectrplus.presentation.viemodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController
) {
    val parentEntry = navController.previousBackStackEntry
    val viewModel: ProfileViewModel =
        if (parentEntry?.destination?.route == "profile") {
            hiltViewModel(parentEntry)
        } else {
            hiltViewModel()
        }
    val state = viewModel.editState
    val profile = viewModel.state.profile
    val fullName = "${state.firstName} ${state.lastName}".ifBlank { "Профиль" }
    val context = LocalContext.current

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.uploadAvatar(context, uri)
    }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Редактировать профиль",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.clickable { pickImage.launch("image/*") }
                ) {
                    val url = profile?.avatarUrl
                    if (!url.isNullOrBlank()) {
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(SpectrColors.PrimarySoft)
                        )
                    } else {
                        InitialsAvatar(name = fullName, size = 100)
                    }
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(SpectrColors.Primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painterResource(R.drawable.baseline_photo_camera_24),
                            contentDescription = "Загрузить фото",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            if (!profile?.avatarUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { viewModel.removeAvatar() }) {
                        Text("Удалить фото", color = SpectrColors.Danger)
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    label = { Text("Имя") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    label = { Text("Фамилия") },
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.phone,
                    onValueChange = viewModel::onPhoneChange,
                    label = { Text("Телефон") },
                    leadingIcon = { Icon(Icons.Default.Phone, null, tint = SpectrColors.TextMuted) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.city,
                    onValueChange = viewModel::onCityChange,
                    label = { Text("Город") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = SpectrColors.TextMuted) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (profile?.accountRole != "SPECIALIST") {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Показывать информацию о ребёнке в профиле",
                                fontSize = 14.sp,
                                color = SpectrColors.Text,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Другие родители увидят город, имя, возраст, пол, диагноз и терапии",
                                fontSize = 12.sp,
                                color = SpectrColors.TextMuted
                            )
                        }
                        Switch(
                            checked = state.showChildInPublicProfile,
                            onCheckedChange = viewModel::onShowChildInPublicProfileChange
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = state.specialistProfession,
                        onValueChange = viewModel::onSpecialistProfessionChange,
                        label = { Text("Профиль (психолог, врач…)") },
                        leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = spectrFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.specialistEducation,
                        onValueChange = viewModel::onSpecialistEducationChange,
                        label = { Text("Образование") },
                        minLines = 2,
                        shape = RoundedCornerShape(16.dp),
                        colors = spectrFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.specialistExperienceYears,
                        onValueChange = viewModel::onSpecialistExperienceYearsChange,
                        label = { Text("Стаж (лет)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = spectrFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                state.error?.let { ErrorBox(it) }
            }

            Spacer(Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.saveProfile(navController) },
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
                        Text("Сохранить", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Отмена", color = SpectrColors.TextMuted)
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
