package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.FormLabel
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.presentation.screen.auth.ErrorBox
import com.example.spectrplus.presentation.viewmodel.profile.ChildViewModel
import com.example.spectrplus.presentation.viewmodel.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddChildScreen(
    navController: NavController,
    viewModel: ChildViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(Unit) { viewModel.reset() }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Добавить ребенка",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SpectrColors.PrimarySoft)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🧒", fontSize = 42.sp)
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            "Информация о ребенке",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpectrColors.Text
                        )
                        Text(
                            "Заполните данные, чтобы мы могли адаптировать контент",
                            fontSize = 12.sp,
                            color = SpectrColors.TextMuted
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Имя ребенка")
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
                    placeholder = { Text("Например, Алексей") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Возраст")
                OutlinedTextField(
                    value = state.age,
                    onValueChange = viewModel::onAgeChange,
                    placeholder = { Text("лет") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Пол")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    GenderOption(
                        emoji = "👦",
                        label = "Мальчик",
                        selected = state.gender == "Мальчик",
                        onClick = { viewModel.onGenderChange("Мальчик") },
                        modifier = Modifier.weight(1f)
                    )
                    GenderOption(
                        emoji = "👧",
                        label = "Девочка",
                        selected = state.gender == "Девочка",
                        onClick = { viewModel.onGenderChange("Девочка") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Диагноз")
                OutlinedTextField(
                    value = state.diagnosis,
                    onValueChange = viewModel::onDiagnosisChange,
                    leadingIcon = { Icon(painterResource(R.drawable.baseline_medical_services_24), null, tint = SpectrColors.TextMuted) },
                    placeholder = { Text("Например, РАС") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Основные особенности")
                OutlinedTextField(
                    value = state.features,
                    onValueChange = viewModel::onFeaturesChange,
                    leadingIcon = { Icon(Icons.Default.Star, null, tint = SpectrColors.TextMuted) },
                    placeholder = { Text("Что важно знать о ребенке") },
                    singleLine = false,
                    minLines = 2,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Заметки родителя")
                OutlinedTextField(
                    value = state.notes,
                    onValueChange = viewModel::onNotesChange,
                    leadingIcon = { Icon(painterResource(R.drawable.baseline_notes_24), null, tint = SpectrColors.TextMuted) },
                    placeholder = { Text("Наблюдения, предпочтения") },
                    singleLine = false,
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TherapiesEditor(
                therapies = state.therapies,
                onAdd = viewModel::addTherapy,
                onRemove = viewModel::removeTherapy,
                onTitleChange = viewModel::onTherapyTitleChange,
                onDescriptionChange = viewModel::onTherapyDescriptionChange
            )

            state.error?.let { ErrorBox(it) }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = {
                    viewModel.addChild {
                        profileViewModel.loadProfile()
                        navController.popBackStack()
                    }
                },
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

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun TherapiesEditor(
    therapies: List<com.example.spectrplus.presentation.state.TherapyInput>,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    onTitleChange: (Int, String) -> Unit,
    onDescriptionChange: (Int, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FormLabel("Текущие терапии")
            Spacer(Modifier.weight(1f))
            Text(
                "+ Добавить",
                color = SpectrColors.Primary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onAdd() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        if (therapies.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SpectrColors.Card)
                    .padding(14.dp)
            ) {
                Text(
                    "Добавьте терапии, которые проходит ребенок (АВА, сенсорная, логопед и т.д.)",
                    fontSize = 12.sp,
                    color = SpectrColors.TextMuted
                )
            }
        } else {
            therapies.forEachIndexed { index, therapy ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SpectrColors.Card)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Терапия ${index + 1}",
                            fontSize = 12.sp,
                            color = SpectrColors.TextMuted,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            "Удалить",
                            color = SpectrColors.Danger,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onRemove(index) }
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    OutlinedTextField(
                        value = therapy.title,
                        onValueChange = { onTitleChange(index, it) },
                        placeholder = { Text("Название, например «АВА-терапия»") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = spectrFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = therapy.description,
                        onValueChange = { onDescriptionChange(index, it) },
                        placeholder = { Text("Описание, частота, специалист") },
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        colors = spectrFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun GenderOption(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bg = if (selected) SpectrColors.Primary else SpectrColors.Card
    val fg = if (selected) Color.White else SpectrColors.Text
    val border = if (selected) SpectrColors.Primary else SpectrColors.Divider

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .border(
                    androidx.compose.foundation.BorderStroke(1.dp, border),
                    RoundedCornerShape(16.dp)
                )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 22.sp)
            Spacer(Modifier.size(8.dp))
            Text(label, color = fg, fontWeight = FontWeight.Medium)
        }
    }
}
