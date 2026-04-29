package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
fun EditChildScreen(
    childId: Long,
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    childViewModel: ChildViewModel = hiltViewModel()
) {
    val profile = profileViewModel.state.profile
    val child = profile?.children?.find { it.id == childId }
    val state = childViewModel.state

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(child?.id) {
        if (child != null && state.editingId != child.id) {
            childViewModel.prefillForEdit(child)
        }
    }

    if (child == null) {
        Scaffold(
            containerColor = SpectrColors.Bg,
            topBar = {
                SpectrTopBar(
                    title = "Редактировать",
                    onBack = { navController.popBackStack() }
                )
            }
        ) { padding ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Ребенок не найден", color = SpectrColors.TextMuted)
            }
        }
        return
    }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Редактировать ребенка",
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, null, tint = SpectrColors.Danger)
                    }
                }
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
                    Text("✏️", fontSize = 42.sp)
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            "Изменение данных",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SpectrColors.Text
                        )
                        Text(
                            "Обновите информацию о ребенке",
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
                    onValueChange = childViewModel::onNameChange,
                    leadingIcon = { Icon(Icons.Default.Person, null, tint = SpectrColors.TextMuted) },
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
                    onValueChange = childViewModel::onAgeChange,
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
                    EditGenderOption(
                        emoji = "👦",
                        label = "Мальчик",
                        selected = state.gender == "Мальчик",
                        onClick = { childViewModel.onGenderChange("Мальчик") },
                        modifier = Modifier.weight(1f)
                    )
                    EditGenderOption(
                        emoji = "👧",
                        label = "Девочка",
                        selected = state.gender == "Девочка",
                        onClick = { childViewModel.onGenderChange("Девочка") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                FormLabel("Диагноз")
                OutlinedTextField(
                    value = state.diagnosis,
                    onValueChange = childViewModel::onDiagnosisChange,
                    leadingIcon = { Icon(painterResource(R.drawable.baseline_medical_services_24), null, tint = SpectrColors.TextMuted) },
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
                    onValueChange = childViewModel::onFeaturesChange,
                    leadingIcon = { Icon(Icons.Default.Star, null, tint = SpectrColors.TextMuted) },
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
                    onValueChange = childViewModel::onNotesChange,
                    leadingIcon = { Icon(painterResource(R.drawable.baseline_notes_24), null, tint = SpectrColors.TextMuted) },
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            TherapiesEditor(
                therapies = state.therapies,
                onAdd = childViewModel::addTherapy,
                onRemove = childViewModel::removeTherapy,
                onTitleChange = childViewModel::onTherapyTitleChange,
                onDescriptionChange = childViewModel::onTherapyDescriptionChange
            )

            state.error?.let { ErrorBox(it) }

            Spacer(Modifier.height(4.dp))

            Button(
                onClick = {
                    childViewModel.updateChild {
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
                    Text("Сохранить изменения", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !state.isLoading
            ) {
                Icon(Icons.Default.Delete, null, tint = SpectrColors.Danger)
                Spacer(Modifier.size(8.dp))
                Text("Удалить ребенка", color = SpectrColors.Danger, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = SpectrColors.Card,
            title = { Text("Удалить ребенка?", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "Все данные о ребенке «${child.name}» будут удалены. Действие нельзя отменить.",
                    color = SpectrColors.TextMuted
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    childViewModel.deleteChild(child.id) {
                        profileViewModel.loadProfile()
                        navController.popBackStack()
                    }
                }) {
                    Text("Удалить", color = SpectrColors.Danger, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Отмена", color = SpectrColors.TextMuted)
                }
            }
        )
    }
}

@Composable
private fun EditGenderOption(
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
            .then(
                Modifier.border(
                    BorderStroke(1.dp, border),
                    RoundedCornerShape(16.dp)
                )
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
