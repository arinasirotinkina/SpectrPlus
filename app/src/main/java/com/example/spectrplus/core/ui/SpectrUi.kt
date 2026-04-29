package com.example.spectrplus.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object SpectrColors {
    val Primary = Color(0xFF5B6DFF)
    val PrimarySoft = Color(0xFFE8EBFF)
    val AccentSoft = Color(0xFFFFF3E0)
    val Success = Color(0xFF4CAF50)
    val SuccessSoft = Color(0xFFE8F5E9)
    val Danger = Color(0xFFEF5350)
    val DangerSoft = Color(0xFFFFEBEE)
    val Bg = Color(0xFFF5F7FD)
    val Card = Color(0xFFFFFFFF)
    val Text = Color(0xFF1F2240)
    val TextMuted = Color(0xFF6B6F8A)
    val Divider = Color(0xFFE6E9F4)

    val HeaderGradient = Brush.linearGradient(
        listOf(Color(0xFF5B6DFF), Color(0xFF8A97FF))
    )
    val CoverGradientA = Brush.linearGradient(
        listOf(Color(0xFF7986CB), Color(0xFF5B6DFF))
    )
    val CoverGradientB = Brush.linearGradient(
        listOf(Color(0xFFFFB74D), Color(0xFFFF8A65))
    )
    val CoverGradientC = Brush.linearGradient(
        listOf(Color(0xFF4DB6AC), Color(0xFF26A69A))
    )
    val CoverGradientD = Brush.linearGradient(
        listOf(Color(0xFFBA68C8), Color(0xFF9575CD))
    )

    val categoryGradients = listOf(
        CoverGradientA, CoverGradientB, CoverGradientC, CoverGradientD
    )
}


object SpectrLabels {

    val articleCategories = linkedMapOf(
        "DIAGNOSTICS" to "Диагностика",
        "CORRECTION_METHODS" to "Методики коррекции",
        "BEHAVIOR" to "Поведение",
        "SPEECH" to "Речь и коммуникация",
        "SENSORY" to "Сенсорная интеграция",
        "SCHOOL" to "Подготовка к школе",
        "LEGAL" to "Правовые вопросы"
    )

    val videoCategories = linkedMapOf(
        "SPEECH" to "Речь",
        "BEHAVIOR" to "Поведение",
        "SENSORY" to "Сенсорика",
        "PARENTING" to "Родителям",
        "SCHOOL_PREP" to "Школа"
    )

    val materialCategories = linkedMapOf(
        "SCHEDULES" to "Расписания",
        "PECS" to "Карточки PECS",
        "CHECKLISTS" to "Чек-листы"
    )

    val forumCategories = linkedMapOf(
        "QUESTIONS" to "Вопросы специалистам",
        "METHODS" to "Методики и терапии",
        "DAILY_LIFE" to "Быт и адаптация",
        "EDUCATION" to "Школа и образование",
        "EMOTIONAL_SUPPORT" to "Эмоциональная поддержка",
        "REGIONAL" to "Региональные сообщества"
    )

    fun article(code: String): String =
        articleCategories[code] ?: code.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
    fun video(code: String): String =
        videoCategories[code] ?: code.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
    fun material(code: String): String =
        materialCategories[code] ?: code.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
    fun forum(code: String): String =
        forumCategories[code] ?: code.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpectrTopBar(
    title: String,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                title,
                fontWeight = FontWeight.SemiBold,
                color = SpectrColors.Text
            )
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад",
                        tint = SpectrColors.Text
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SpectrColors.Bg,
            titleContentColor = SpectrColors.Text
        )
    )
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    action: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SpectrColors.Text
            )
            if (subtitle != null) {
                Text(
                    subtitle,
                    fontSize = 13.sp,
                    color = SpectrColors.TextMuted
                )
            }
        }
        if (action != null) action()
    }
}

@Composable
fun InitialsAvatar(
    name: String,
    size: Int = 56,
    background: Brush = SpectrColors.HeaderGradient
) {
    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "👤" }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            initials,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size / 2.6).sp
        )
    }
}

@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (selected) SpectrColors.Primary else SpectrColors.Card
    val fg = if (selected) Color.White else SpectrColors.Text
    val border = if (selected) SpectrColors.Primary else SpectrColors.Divider

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .border(
                    BorderStroke(1.dp, border),
                    RoundedCornerShape(20.dp)
                )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(label, color = fg, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun LoadingBlock(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = SpectrColors.Primary)
    }
}

@Composable
fun EmptyState(
    emoji: String,
    title: String,
    subtitle: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(emoji, fontSize = 48.sp)
        Text(
            title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = SpectrColors.Text
        )
        if (subtitle != null) {
            Text(
                subtitle,
                fontSize = 13.sp,
                color = SpectrColors.TextMuted
            )
        }
    }
}

@Composable
fun InfoPill(
    label: String,
    color: Color = SpectrColors.PrimarySoft,
    textColor: Color = SpectrColors.Primary
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun FormLabel(text: String) {
    Text(
        text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = SpectrColors.TextMuted,
        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
    )
}

@Composable
fun spectrFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SpectrColors.Primary,
    unfocusedBorderColor = SpectrColors.Divider,
    cursorColor = SpectrColors.Primary,
    focusedLabelColor = SpectrColors.Primary,
    unfocusedLabelColor = SpectrColors.TextMuted,
    focusedContainerColor = SpectrColors.Card,
    unfocusedContainerColor = SpectrColors.Card
)
