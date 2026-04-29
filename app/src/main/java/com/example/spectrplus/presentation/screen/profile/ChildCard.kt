package com.example.spectrplus.presentation.screen.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spectrplus.R
import com.example.spectrplus.core.ui.InfoPill
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.domain.model.profile.Child

@Composable
fun ChildCard(
    child: Child,
    onEdit: (() -> Unit)? = null
) {

    val gender = child.gender.trim().lowercase()
    val emoji = when {
        gender.startsWith("м") || gender.startsWith("m") -> "👦"
        gender.startsWith("ж") || gender.startsWith("f") || gender.startsWith("w") -> "👧"
        else -> "🧒"
    }

    val cardModifier = Modifier
        .fillMaxWidth()
        .let { if (onEdit != null) it.clickable { onEdit() } else it }

    ElevatedCard(
        modifier = cardModifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(SpectrColors.PrimarySoft),
                    contentAlignment = Alignment.Center
                ) {
                    Text(emoji, fontSize = 28.sp)
                }

                Spacer(Modifier.size(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        child.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SpectrColors.Text
                    )
                    Spacer(Modifier.size(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        InfoPill(
                            label = declineAge(child.age),
                            color = SpectrColors.PrimarySoft,
                            textColor = SpectrColors.Primary
                        )
                        if (child.gender.isNotBlank()) {
                            InfoPill(
                                label = child.gender,
                                color = SpectrColors.AccentSoft,
                                textColor = Color(0xFFE65100)
                            )
                        }
                    }
                }

                if (onEdit != null) {
                    IconButton(onClick = onEdit) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SpectrColors.PrimarySoft),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = SpectrColors.Primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            if (child.diagnosis.isNotBlank()) {
                Spacer(Modifier.size(14.dp))
                InfoRow(
                    iconId = R.drawable.baseline_medical_services_24,
                    title = "Диагноз",
                    value = child.diagnosis
                )
            }

            if (child.features.isNotBlank()) {
                Spacer(Modifier.size(10.dp))
                InfoRow(
                    iconId = R.drawable.baseline_favorite_24,
                    title = "Особенности",
                    value = child.features
                )
            }

            if (child.notes.isNotBlank()) {
                Spacer(Modifier.size(10.dp))
                InfoRow(
                    iconId = R.drawable.baseline_notes_24,
                    title = "Заметки родителя",
                    value = child.notes
                )
            }

            if (child.therapies.isNotEmpty()) {
                Spacer(Modifier.size(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(SpectrColors.Divider)
                )
                Spacer(Modifier.size(12.dp))
                Text(
                    "Текущие терапии",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SpectrColors.TextMuted
                )
                Spacer(Modifier.size(6.dp))
                child.therapies.forEach { therapy ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(SpectrColors.Primary, CircleShape)
                                .padding(top = 6.dp)
                        )
                        Spacer(Modifier.size(8.dp))
                        Column {
                            Text(
                                therapy.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = SpectrColors.Text
                            )
                            if (therapy.description.isNotBlank()) {
                                Text(
                                    therapy.description,
                                    fontSize = 12.sp,
                                    color = SpectrColors.TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    @DrawableRes iconId: Int,
    title: String,
    value: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SpectrColors.PrimarySoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(painterResource(iconId), null, tint = SpectrColors.Primary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.size(10.dp))
        Column {
            Text(title, fontSize = 12.sp, color = SpectrColors.TextMuted)
            Text(value, fontSize = 14.sp, color = SpectrColors.Text)
        }
    }
}

private fun declineAge(age: Int): String {
    val n = age % 100
    val last = age % 10
    val word = when {
        n in 11..14 -> "лет"
        last == 1 -> "год"
        last in 2..4 -> "года"
        else -> "лет"
    }
    return "$age $word"
}
