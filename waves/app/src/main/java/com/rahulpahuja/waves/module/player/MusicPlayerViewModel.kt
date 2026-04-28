package com.rahulpahuja.waves.module.player

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.rahulpahuja.waves.data.local.AudioFile
import com.rahulpahuja.waves.data.local.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MusicPlayerUiState(
    val songs: List<AudioFile> = emptyList(),
    val filteredSongs: List<AudioFile> = emptyList(),
    val searchQuery: String = "",
    val currentSong: AudioFile? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val hasPermission: Boolean = false
)

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val repository: MediaRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicPlayerUiState())
    val uiState: StateFlow<MusicPlayerUiState> = _uiState.asStateFlow()

    private var mediaController: MediaController? = null

    init {
        initializeMediaController()
    }

    private fun initializeMediaController() {
        viewModelScope.launch {
            val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
            val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
            mediaController = controllerFuture.await()
            
            mediaController?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _uiState.update { it.copy(isPlaying = isPlaying) }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    val currentSongId = mediaItem?.mediaId?.toLongOrNull()
                    val currentSong = _uiState.value.songs.find { it.id == currentSongId }
                    _uiState.update { it.copy(currentSong = currentSong) }
                }
            })
        }
    }

    fun onPermissionGranted() {
        _uiState.update { it.copy(hasPermission = true) }
        loadSongs()
    }

    private fun loadSongs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val songs = repository.getLocalAudioFiles()
            _uiState.update { it.copy(songs = songs, filteredSongs = songs, isLoading = false) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val filtered = state.songs.filter {
                it.title.contains(query, ignoreCase = true) || it.artist.contains(query, ignoreCase = true)
            }
            state.copy(searchQuery = query, filteredSongs = filtered)
        }
    }

    fun playSong(song: AudioFile) {
        val mediaItem = MediaItem.Builder()
            .setMediaId(song.id.toString())
            .setUri(song.contentUri)
            .build()
        
        mediaController?.let { controller ->
            controller.setMediaItem(mediaItem)
            controller.prepare()
            controller.play()
            _uiState.update { it.copy(currentSong = song) }
        }
    }

    fun togglePlayPause() {
        mediaController?.let { controller ->
            if (controller.isPlaying) {
                controller.pause()
            } else {
                controller.play()
            }
        }
    }

    fun skipToNext() {
        mediaController?.seekToNext()
    }

    fun skipToPrevious() {
        mediaController?.seekToPrevious()
    }

    override fun onCleared() {
        super.onCleared()
        mediaController?.release()
    }
}
