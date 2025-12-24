package com.rahulpahuja.waves.module.gallery

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaGalleryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MediaGalleryUiState())
    val uiState: StateFlow<MediaGalleryUiState> = _uiState.asStateFlow()

    private val allUploads = listOf(
        MediaUpload("1", "Pioneer Setup Demo", "Oct 24, 2023", "Gear", Color(0xFF2962FF), 1.5f),
        MediaUpload("2", "Neon Night Live", "Oct 22, 2023", "Event", Color(0xFF9C27B0), 1.0f),
        MediaUpload("3", "Final Showcase 2023", "Sep 15, 2023", "Graduation", Color(0xFF00E676), 1.2f),
        MediaUpload("4", "Studio B Upgrade", "Sep 12, 2023", "Gear", Color(0xFF2962FF), 0.8f),
        MediaUpload("5", "Vinyl Collection", "Aug 30, 2023", "Gear", Color(0xFF2962FF), 1.0f),
        MediaUpload("6", "Backstage Access", "Aug 28, 2023", "Event", Color(0xFF9C27B0), 1.3f)
    )

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    highlights = listOf(
                        Highlight("1", "Summer Bash", 124, Color(0xFFE91E63)),
                        Highlight("2", "Pro Gear Setup", 45, Color(0xFFFFA000)),
                        Highlight("3", "Class of '23", 89, Color(0xFF00E676))
                    ),
                    recentUploads = allUploads
                )
            }
        }
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { state ->
            val filtered = if (filter == "All") {
                allUploads
            } else {
                allUploads.filter { it.category.equals(filter, ignoreCase = true) }
            }
            state.copy(selectedFilter = filter, recentUploads = filtered)
        }
    }
}

data class MediaGalleryUiState(
    val filters: List<String> = listOf("All", "Events", "Gear", "Graduation"),
    val selectedFilter: String = "All",
    val highlights: List<Highlight> = emptyList(),
    val recentUploads: List<MediaUpload> = emptyList()
)

data class Highlight(
    val id: String,
    val title: String,
    val count: Int,
    val color: Color
)

data class MediaUpload(
    val id: String,
    val title: String,
    val date: String,
    val category: String,
    val categoryColor: Color,
    val aspectRatio: Float
)
