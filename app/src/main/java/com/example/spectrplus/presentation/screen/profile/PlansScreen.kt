package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.domain.model.profile.Plan
import com.example.spectrplus.presentation.viemodel.profile.PlansViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlansScreen(
    navController: NavController,
    viewModel: PlansViewModel = hiltViewModel()
) {

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentMonth) { viewModel.load(currentMonth) }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Планы",
                onBack = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SpectrColors.Primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, null)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                CalendarCard(
                    month = currentMonth,
                    selected = selectedDate,
                    plans = viewModel.plans,
                    onPrev = { currentMonth = currentMonth.minusMonths(1) },
                    onNext = { currentMonth = currentMonth.plusMonths(1) },
                    onSelect = { selectedDate = it }
                )
            }

            val todays = viewModel.plans.filter { it.date == selectedDate }

            item {
                Text(
                    "Задачи на ${
                        "%d %s".format(
                            selectedDate.dayOfMonth,
                            selectedDate.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru"))
                        )
                    }",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.Text
                )
            }

            if (todays.isEmpty()) {
                item {
                    EmptyState(
                        emoji = "📅",
                        title = "На этот день нет задач",
                        subtitle = "Нажмите +, чтобы добавить событие"
                    )
                }
            } else {
                items(todays, key = { it.id }) { task ->
                    TaskRow(
                        task = task,
                        onToggle = { viewModel.toggleDone(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddPlanDialog(
            date = selectedDate,
            onDismiss = { showAddDialog = false },
            onSave = { title, time ->
                viewModel.addTask(selectedDate, title, time)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddPlanDialog(
    date: LocalDate,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("09:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SpectrColors.Card,
        title = {
            Text(
                "Новое событие на ${date.dayOfMonth}.${"%02d".format(date.monthValue)}",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Время (HH:mm)") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = spectrFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(title, time) }, enabled = title.isNotBlank()) {
                Text("Добавить", color = SpectrColors.Primary, fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = SpectrColors.TextMuted)
            }
        }
    )
}

@Composable
private fun CalendarCard(
    month: YearMonth,
    selected: LocalDate,
    plans: List<Plan>,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onSelect: (LocalDate) -> Unit
) {
    val datesWithTasks = remember(plans) { plans.map { it.date }.toSet() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SpectrColors.Card)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrev) {
                Icon(painterResource(R.drawable.baseline_chevron_left_24), null, tint = SpectrColors.Primary)
            }
            Text(
                "${month.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")).replaceFirstChar { it.uppercase() }} ${month.year}",
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = SpectrColors.Text,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(onClick = onNext) {
                Icon(painterResource(R.drawable.baseline_chevron_right_24), null, tint = SpectrColors.Primary)
            }
        }

        Spacer(Modifier.size(8.dp))

        val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
        Row(Modifier.fillMaxWidth()) {
            weekdays.forEach {
                Text(
                    it,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 12.sp,
                    color = SpectrColors.TextMuted
                )
            }
        }

        Spacer(Modifier.size(4.dp))

        val firstDay = month.atDay(1)
        val dayOfWeek = (firstDay.dayOfWeek.value - 1).coerceAtLeast(0)
        val daysInMonth = month.lengthOfMonth()
        val cells = dayOfWeek + daysInMonth
        val rows = (cells + 6) / 7

        for (row in 0 until rows) {
            Row(Modifier.fillMaxWidth()) {
                for (col in 0 until 7) {
                    val idx = row * 7 + col
                    val day = idx - dayOfWeek + 1
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (day in 1..daysInMonth) {
                            val date = month.atDay(day)
                            val isSelected = date == selected
                            val isToday = date == LocalDate.now()
                            val hasTasks = date in datesWithTasks
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSelected -> SpectrColors.Primary
                                            isToday -> SpectrColors.PrimarySoft
                                            else -> Color.Transparent
                                        }
                                    )
                                    .clickable { onSelect(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$day",
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected || isToday) FontWeight.SemiBold else FontWeight.Normal,
                                    color = when {
                                        isSelected -> Color.White
                                        isToday -> SpectrColors.Primary
                                        else -> SpectrColors.Text
                                    }
                                )
                                if (hasTasks && !isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomCenter)
                                            .size(4.dp)
                                            .clip(CircleShape)
                                            .background(SpectrColors.Primary)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: Plan,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SpectrColors.Card)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.done,
            onCheckedChange = { onToggle() },
            colors = CheckboxDefaults.colors(
                checkedColor = SpectrColors.Primary,
                uncheckedColor = SpectrColors.TextMuted
            )
        )
        Column(Modifier.weight(1f)) {
            Text(
                task.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (task.done) SpectrColors.TextMuted else SpectrColors.Text,
                textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                task.time,
                fontSize = 12.sp,
                color = SpectrColors.TextMuted
            )
        }
        IconButton(onClick = onDelete) {
            Text("✕", color = SpectrColors.TextMuted, fontSize = 14.sp)
        }
    }
}
