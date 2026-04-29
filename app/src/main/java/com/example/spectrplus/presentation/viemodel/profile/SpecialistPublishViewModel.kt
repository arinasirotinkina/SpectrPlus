package com.example.spectrplus.presentation.viemodel.profile

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spectrplus.domain.interactor.profile.SpecialistInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecialistPublishViewModel @Inject constructor(
    private val specialistInteractor: SpecialistInteractor
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set
    var message by mutableStateOf<String?>(null)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    fun clearFeedback() {
        message = null
        error = null
    }

    fun uploadUri(context: Context, uri: Uri, folder: String, onDone: (String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val resolver = context.contentResolver
                val mime = resolver.getType(uri) ?: "application/octet-stream"
                val ext = when {
                    mime.contains("pdf") -> "pdf"
                    mime.contains("png") -> "png"
                    mime.contains("jpeg") || mime.contains("jpg") -> "jpg"
                    else -> "bin"
                }
                val name = "upload_${System.currentTimeMillis()}.$ext"
                val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw IllegalStateException("Не удалось прочитать файл")
                val url = specialistInteractor.uploadFile(folder, name, mime, bytes)
                onDone(url)
                message = "Файл загружен"
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }

    fun publishArticle(
        title: String,
        content: String,
        category: String,
        source: String?,
        coverUrl: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                specialistInteractor.createArticle(title, content, category, source, coverUrl)
                message = "Статья опубликована"
                onSuccess()
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }

    fun publishVideo(
        title: String,
        description: String,
        videoUrl: String,
        thumbnailUrl: String,
        durationSeconds: Int,
        category: String,
        source: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                specialistInteractor.createVideo(
                    title, description, videoUrl, thumbnailUrl, durationSeconds, category, source
                )
                message = "Видео добавлено"
                onSuccess()
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }

    fun publishMaterial(
        title: String,
        description: String,
        fileUrl: String,
        type: String,
        category: String,
        fileSize: Long,
        source: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                specialistInteractor.createMaterial(title, description, fileUrl, type, category, fileSize, source)
                message = "Материал добавлен"
                onSuccess()
            } catch (e: Exception) {
                error = e.message
            }
            isLoading = false
        }
    }
}
