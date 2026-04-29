package com.example.spectrplus.presentation.screen.social

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.spectrplus.core.ui.SpectrColors
import com.example.spectrplus.core.ui.SpectrTopBar
import com.example.spectrplus.core.ui.spectrFieldColors
import com.example.spectrplus.presentation.screen.auth.ErrorBox
import com.example.spectrplus.presentation.viewmodel.profile.SpecialistPublishViewModel

private val educationCategoryOptions = listOf(
    "DIAGNOSTICS" to "Диагностика",
    "CORRECTION_METHODS" to "Методы коррекции",
    "BEHAVIOR" to "Поведение и эмоции",
    "SPEECH" to "Речь и коммуникация",
    "SENSORY" to "Сенсорика",
    "SCHOOL_PREP" to "Подготовка к школе",
    "LEGAL" to "Правовая поддержка"
)

private val materialCategoryOptions = listOf(
    "VISUAL_SCHEDULE" to "Визуальное расписание",
    "PECS" to "PECS и альтернативная коммуникация",
    "CHECKLISTS" to "Чек-листы"
)

private val materialTypeOptions = listOf(
    "PDF" to "PDF",
    "IMAGE" to "Изображение",
    "DOC" to "Документ"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RussianApiDropdown(
    label: String,
    selectedApi: String,
    options: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val labelText = options.find { it.first == selectedApi }?.second ?: selectedApi
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = labelText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            shape = RoundedCornerShape(16.dp),
            colors = spectrFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (api, ru) ->
                DropdownMenuItem(
                    text = { Text(ru) },
                    onClick = {
                        onSelect(api)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialistPublishScreen() {
    val viewModel: SpecialistPublishViewModel = hiltViewModel()
    val context = LocalContext.current
    var tab by remember { mutableIntStateOf(0) }

    var artTitle by remember { mutableStateOf("") }
    var artContent by remember { mutableStateOf("") }
    var artCategory by remember { mutableStateOf("SPEECH") }
    var artSource by remember { mutableStateOf("") }
    var artCoverUrl by remember { mutableStateOf("") }

    var vidTitle by remember { mutableStateOf("") }
    var vidDesc by remember { mutableStateOf("") }
    var vidUrl by remember { mutableStateOf("") }
    var vidThumb by remember { mutableStateOf("") }
    var vidDuration by remember { mutableStateOf("120") }
    var vidCategory by remember { mutableStateOf("SPEECH") }
    var vidSource by remember { mutableStateOf("") }

    var matTitle by remember { mutableStateOf("") }
    var matDesc by remember { mutableStateOf("") }
    var matFileUrl by remember { mutableStateOf("") }
    var matType by remember { mutableStateOf("PDF") }
    var matCategory by remember { mutableStateOf("VISUAL_SCHEDULE") }
    var matSource by remember { mutableStateOf("") }

    val pickCover = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.uploadUri(context, uri, "covers") { artCoverUrl = it }
        }
    }
    val pickMaterial = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.uploadUri(context, uri, "materials") { matFileUrl = it }
        }
    }

    Scaffold(
        containerColor = SpectrColors.Bg,
        topBar = { SpectrTopBar(title = "Добавить контент") }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, onClick = { tab = 0 }, text = { Text("Статья") })
                Tab(selected = tab == 1, onClick = { tab = 1 }, text = { Text("Видео") })
                Tab(selected = tab == 2, onClick = { tab = 2 }, text = { Text("Материал") })
            }
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                viewModel.error?.let { ErrorBox(it) }
                viewModel.message?.let {
                    Text(it, color = SpectrColors.Success, fontSize = 14.sp)
                }

                when (tab) {
                    0 -> {
                        OutlinedTextField(
                            value = artTitle,
                            onValueChange = { artTitle = it },
                            label = { Text("Заголовок") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = spectrFieldColors()
                        )
                        OutlinedTextField(
                            value = artContent,
                            onValueChange = { artContent = it },
                            label = { Text("Текст статьи") },
                            minLines = 5,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = spectrFieldColors()
                        )
                        RussianApiDropdown(
                            label = "Категория",
                            selectedApi = artCategory,
                            options = educationCategoryOptions,
                            onSelect = { artCategory = it }
                        )
                        OutlinedTextField(
                            value = artSource,
                            onValueChange = { artSource = it },
                            label = { Text("Источник / автор оригинала (если не вы)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = spectrFieldColors()
                        )
                        OutlinedTextField(
                            value = artCoverUrl,
                            onValueChange = { artCoverUrl = it },
                            label = { Text("URL обложки (или загрузите)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = spectrFieldColors()
                        )
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Button(
                                onClick = { pickCover.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                            ) { Text("Загрузить обложку с устройства") }
                        }
                        Button(
                            onClick = {
                                viewModel.clearFeedback()
                                viewModel.publishArticle(
                                    artTitle, artContent, artCategory,
                                    artSource.takeIf { it.isNotBlank() },
                                    artCoverUrl.takeIf { it.isNotBlank() }
                                ) {
                                    artTitle = ""
                                    artContent = ""
                                    artSource = ""
                                    artCoverUrl = ""
                                }
                            },
                            enabled = !viewModel.isLoading && artTitle.isNotBlank() && artContent.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Опубликовать статью", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    1 -> {
                        OutlinedTextField(vidTitle, { vidTitle = it }, label = { Text("Название") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(vidDesc, { vidDesc = it }, label = { Text("Описание") }, minLines = 2, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(vidUrl, { vidUrl = it }, label = { Text("URL видео (mp4 и т.п.)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(vidThumb, { vidThumb = it }, label = { Text("URL превью") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(vidDuration, { vidDuration = it.filter { ch -> ch.isDigit() } }, label = { Text("Длительность, сек") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        RussianApiDropdown(
                            label = "Категория",
                            selectedApi = vidCategory,
                            options = educationCategoryOptions,
                            onSelect = { vidCategory = it }
                        )
                        OutlinedTextField(vidSource, { vidSource = it }, label = { Text("Источник") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        Button(
                            onClick = {
                                viewModel.clearFeedback()
                                viewModel.publishVideo(
                                    vidTitle, vidDesc, vidUrl, vidThumb,
                                    vidDuration.toIntOrNull() ?: 0,
                                    vidCategory,
                                    vidSource.takeIf { it.isNotBlank() }
                                ) {
                                    vidTitle = ""
                                    vidDesc = ""
                                    vidUrl = ""
                                    vidThumb = ""
                                    vidSource = ""
                                }
                            },
                            enabled = !viewModel.isLoading && vidTitle.isNotBlank() && vidUrl.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Добавить видео", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    else -> {
                        OutlinedTextField(matTitle, { matTitle = it }, label = { Text("Название") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(matDesc, { matDesc = it }, label = { Text("Описание") }, minLines = 2, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        OutlinedTextField(matFileUrl, { matFileUrl = it }, label = { Text("URL файла") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        RussianApiDropdown(
                            label = "Тип файла",
                            selectedApi = matType,
                            options = materialTypeOptions,
                            onSelect = { matType = it }
                        )
                        RussianApiDropdown(
                            label = "Категория",
                            selectedApi = matCategory,
                            options = materialCategoryOptions,
                            onSelect = { matCategory = it }
                        )
                        OutlinedTextField(matSource, { matSource = it }, label = { Text("Источник") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = spectrFieldColors())
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Button(
                                onClick = { pickMaterial.launch("*/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                            ) { Text("Загрузить файл с устройства") }
                        }
                        Button(
                            onClick = {
                                viewModel.clearFeedback()
                                viewModel.publishMaterial(
                                    matTitle, matDesc, matFileUrl, matType, matCategory,
                                    0L,
                                    matSource.takeIf { it.isNotBlank() }
                                ) {
                                    matTitle = ""
                                    matDesc = ""
                                    matFileUrl = ""
                                    matSource = ""
                                }
                            },
                            enabled = !viewModel.isLoading && matTitle.isNotBlank() && matFileUrl.isNotBlank(),
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SpectrColors.Primary)
                        ) {
                            if (viewModel.isLoading) {
                                CircularProgressIndicator(
                                    Modifier.size(22.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Добавить материал", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
