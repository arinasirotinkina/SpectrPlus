package com.example.spectrplus.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import android.content.Intent
import android.net.Uri
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(navController: NavController) {
    val context = LocalContext.current
    var showAbout by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = {
            SpectrTopBar(
                title = "Поддержка",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(SpectrColors.HeaderGradient)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💬", fontSize = 40.sp)
                    Spacer(Modifier.size(12.dp))
                    Column {
                        Text(
                            "Мы всегда рядом",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Свяжитесь с командой разработки",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            ContactItem(
                icon = Icons.Default.Email,
                title = "Электронная почта",
                subtitle = "support@spectrplus.app",
                color = SpectrColors.Primary,
                background = SpectrColors.PrimarySoft
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@spectrplus.app")
                    putExtra(Intent.EXTRA_SUBJECT, "Вопрос по СпектрПлюс")
                }
                runCatching { context.startActivity(intent) }
            }

            ContactItem(
                icon = Icons.Default.Phone,
                title = "Телефон",
                subtitle = "+7 (800) 555-35-35",
                color = SpectrColors.Success,
                background = SpectrColors.SuccessSoft
            ) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:+78005553535")
                }
                runCatching { context.startActivity(intent) }
            }

            ContactItem(
                icon = Icons.Default.Info,
                title = "О приложении",
                subtitle = "Версия 1.0.0 · СпектрПлюс",
                color = Color(0xFFE65100),
                background = SpectrColors.AccentSoft
            ) { showAbout = true }
        }
    }

    if (showAbout) {
        AlertDialog(
            onDismissRequest = { showAbout = false },
            containerColor = SpectrColors.Card,
            title = { Text("СпектрПлюс", fontWeight = FontWeight.SemiBold) },
            text = {
                Text(
                    "Приложение для родителей детей с особенностями развития.\n\nВерсия 1.0.0",
                    color = SpectrColors.TextMuted
                )
            },
            confirmButton = {
                TextButton(onClick = { showAbout = false }) {
                    Text("Закрыть", color = SpectrColors.Primary, fontWeight = FontWeight.SemiBold)
                }
            }
        )
    }
}

@Composable
private fun ContactItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    background: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(SpectrColors.Card)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(background),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color)
        }
        Spacer(Modifier.size(12.dp))
        Column {
            Text(
                title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = SpectrColors.Text
            )
            Text(
                subtitle,
                fontSize = 13.sp,
                color = SpectrColors.TextMuted
            )
        }
    }
}
