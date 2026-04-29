package com.example.spectrplus.presentation.screen.education

import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.spectrplus.R
import com.example.spectrplus.core.datastore.FileDownloader
import com.example.spectrplus.core.ui.CategoryChip
import com.example.spectrplus.core.ui.EmptyState
import com.example.spectrplus.core.ui.LoadingBlock
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrLabels
import com.example.spectrplus.domain.model.education.Material
import com.example.spectrplus.presentation.viewmodel.education.MaterialsViewModel
import java.io.File

@Composable
fun MaterialsSection(
    viewModel: MaterialsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    val downloader = remember { FileDownloader(context) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize()) {

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
        ) {
            item {
                CategoryChip(
                    label = "Все",
                    selected = true,
                    onClick = { viewModel.load(null) }
                )
            }
            items(SpectrLabels.materialCategories.entries.toList()) { e ->
                CategoryChip(
                    label = e.value,
                    selected = false,
                    onClick = { viewModel.load(e.key) }
                )
            }
        }

        when {
            state.isLoading -> LoadingBlock()
            state.materials.isEmpty() -> EmptyState(
                emoji = "📄",
                title = "Материалов пока нет",
                subtitle = "Выберите другую категорию"
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.materials) { material ->
                    MaterialItem(
                        material = material,
                        onDownload = {
                            downloader.downloadFile(material.fileUrl, material.title)
                        },
                        onOpen = {
                            val file = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                material.title
                            )
                            downloader.openFileByUrl(context, material.fileUrl)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MaterialItem(
    material: Material,
    onDownload: () -> Unit,
    onOpen: () -> Unit
) {
    val (icon, tint, bg) = materialIconFor(material.category)

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = SpectrColors.Card),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(painterResource(icon), null, tint = tint, modifier = Modifier.size(24.dp))
                }

                Spacer(Modifier.size(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        material.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SpectrColors.Text,
                        maxLines = 2
                    )
                    Text(
                        SpectrLabels.material(material.category),
                        fontSize = 12.sp,
                        color = SpectrColors.TextMuted
                    )
                }
            }

            if (material.description.isNotBlank()) {
                Spacer(Modifier.size(10.dp))
                Text(
                    material.description,
                    fontSize = 13.sp,
                    color = SpectrColors.TextMuted,
                    maxLines = 3
                )
            }

            Spacer(Modifier.size(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onDownload,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                ) {
                    Icon(painterResource(R.drawable.baseline_download_24), null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.size(6.dp))
                    Text("Скачать", fontSize = 14.sp)
                }
                OutlinedButton(
                    onClick = onOpen,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(painterResource(R.drawable.baseline_open_in_new_24), null, modifier = Modifier.size(18.dp), tint = SpectrColors.Primary)
                    Spacer(Modifier.size(6.dp))
                    Text("Открыть", fontSize = 14.sp, color = SpectrColors.Primary)
                }
            }
        }
    }
}

private fun materialIconFor(category: String): Triple<Int, Color, Color> {
    return when (category.uppercase()) {
        "SCHEDULES" -> Triple(R.drawable.baseline_schedule_24, Color(0xFF1976D2), Color(0xFFE3F2FD))
        "PECS" -> Triple(R.drawable.baseline_style_24, Color(0xFFE65100), SpectrColors.AccentSoft)
        "CHECKLISTS" -> Triple(R.drawable.baseline_checklist_24, SpectrColors.Success, SpectrColors.SuccessSoft)
        else -> Triple(R.drawable.baseline_description_24, SpectrColors.Primary, SpectrColors.PrimarySoft)
    }
}
