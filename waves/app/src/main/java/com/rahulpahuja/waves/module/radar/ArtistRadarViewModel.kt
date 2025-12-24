package com.rahulpahuja.waves.module.radar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistRadarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistRadarUiState())
    val uiState: StateFlow<ArtistRadarUiState> = _uiState.asStateFlow()

    init {
        loadGigs()
    }

    private fun loadGigs() {
        viewModelScope.launch {
            _uiState.value = ArtistRadarUiState(
                upcomingGigs = listOf(
                    Gig(
                        id = "1",
                        title = "Techno Bunker Night",
                        artist = "DJ Mandy",
                        location = "The Warehouse, Berlin",
                        time = "23:00 - 01:00",
                        tags = listOf("Acid Techno"),
                        date = "NOV 12",
                        isLive = true,
                        attendees = 5
                    ),
                    Gig(
                        id = "2",
                        title = "Deep House Sunday",
                        artist = "Sarah Bass",
                        location = "Roof Garden, London",
                        time = "16:00 - 18:00",
                        tags = listOf("Deep House"),
                        date = "NOV 14",
                        isLive = false,
                        attendees = 0
                    )
                ),
                pastGigs = listOf(
                    Gig(
                        id = "3",
                        title = "Open Deck Night",
                        artist = "Multiple Artists",
                        location = "Vinyl Bar",
                        time = "",
                        tags = emptyList(),
                        date = "OCT 30",
                        isLive = false,
                        attendees = 0,
                        isCompleted = true
                    )
                )
            )
        }
    }
}

data class ArtistRadarUiState(
    val upcomingGigs: List<Gig> = emptyList(),
    val pastGigs: List<Gig> = emptyList(),
    val selectedFilter: String = "All Gigs"
)

data class Gig(
    val id: String,
    val title: String,
    val artist: String,
    val location: String,
    val time: String,
    val tags: List<String>,
    val date: String,
    val isLive: Boolean,
    val attendees: Int, // +5 representation
    val isCompleted: Boolean = false
)
