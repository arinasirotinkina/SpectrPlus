package com.example.spectrplus.presentation.viewmodel.education

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.spectrplus.domain.model.education.Video
import com.example.spectrplus.domain.interactor.education.VideoInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoInteractor: VideoInteractor
) : ViewModel() {

    var state by mutableStateOf(VideoState())
        private set

    var selectedVideo by mutableStateOf<Video?>(null)
        private set

    var favorites by mutableStateOf<List<Video>>(emptyList())
        private set

    fun loadById(id: Long) {
        viewModelScope.launch {
            try {
                selectedVideo = videoInteractor.getById(id)
            } catch (e: Exception) {
                selectedVideo = null
            }
        }
    }

    fun load(category: String? = null, maxDuration: Int? = null) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            try {
                val videos = videoInteractor.getVideos(category, maxDuration)
                state = state.copy(
                    videos = videos,
                    isLoading = false
                )
            } catch (e: Exception) {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                favorites = videoInteractor.getFavorites()
            } catch (e: Exception) {
                favorites = emptyList()
            }
        }
    }

    fun toggleFavorite(videoId: Long) {
        viewModelScope.launch {
            val prev = state.videos.find { it.id == videoId }?.isFavorite
                ?: selectedVideo?.takeIf { it.id == videoId }?.isFavorite
                ?: favorites.any { it.id == videoId }
            val newFavorite = !prev
            try {
                videoInteractor.toggleFavorite(videoId)
                state = state.copy(
                    videos = state.videos.map {
                        if (it.id == videoId) it.copy(isFavorite = newFavorite) else it
                    }
                )
                selectedVideo?.let { current ->
                    if (current.id == videoId) {
                        selectedVideo = current.copy(isFavorite = newFavorite)
                    }
                }
                loadFavorites()
            } catch (_: Exception) {
                state = state.copy(
                    videos = state.videos.map {
                        if (it.id == videoId) {
                            it.copy(isFavorite = prev)
                        } else it
                    }
                )
                selectedVideo?.let { current ->
                    if (current.id == videoId) {
                        selectedVideo = current.copy(isFavorite = prev)
                    }
                }
            }
        }
    }

    fun markWatched(videoId: Long) {
        viewModelScope.launch {
            try {
                videoInteractor.markWatched(videoId)
                state = state.copy(
                    videos = state.videos.map {
                        if (it.id == videoId) it.copy(isWatched = true) else it
                    }
                )
                selectedVideo?.let { current ->
                    if (current.id == videoId) {
                        selectedVideo = current.copy(isWatched = true)
                    }
                }
            } catch (_: Exception) {
            }
        }
    }
}

data class VideoState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false
)

@UnstableApi
object VideoPlayerFactory {

    private var cache: SimpleCache? = null

    fun getPlayer(context: Context): ExoPlayer {

        if (cache == null) {
            val cacheDir = File(context.cacheDir, "video_cache")
            cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            )
        }

        val dataSourceFactory = DefaultDataSource.Factory(context)

        return ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(dataSourceFactory)
            )
            .build()
    }
}
