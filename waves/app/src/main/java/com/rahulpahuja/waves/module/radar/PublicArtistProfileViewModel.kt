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
class PublicArtistProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PublicArtistProfileUiState())
    val uiState: StateFlow<PublicArtistProfileUiState> = _uiState.asStateFlow()

    init {
        loadArtistProfile()
    }

    private fun loadArtistProfile() {
        viewModelScope.launch {
            _uiState.value = PublicArtistProfileUiState(
                name = "DJ Hyperion",
                location = "San Francisco, CA",
                genres = listOf("Techno", "Deep House", "Minimal"),
                about = "Emerging from the underground scene of the Bay Area, DJ Hyperion blends deep, driving basslines with ethereal synths to create immersive sonic landscapes. A dedicated student of the beat, constantly evolving his sound through live experimentation and studio production.",
                socials = listOf(
                    SocialLink("Instagram", "url"),
                    SocialLink("Facebook", "url"),
                    SocialLink("SoundCloud", "url"),
                    SocialLink("Spotify", "url")
                ),
                gigHistory = listOf(
                    GigHistoryItem("1", "SEP", "12", "Warehouse 404", "The Underground • Oakland", "Opening Set"),
                    GigHistoryItem("2", "AUG", "28", "Summer Bass Fest", "Golden Gate Park", "Student Showcase")
                )
            )
        }
    }

    fun followArtist() {
        // Logic to follow
    }

    fun bookArtist() {
        // Logic to book
    }
}

data class PublicArtistProfileUiState(
    val name: String = "",
    val location: String = "",
    val genres: List<String> = emptyList(),
    val about: String = "",
    val socials: List<SocialLink> = emptyList(),
    val gigHistory: List<GigHistoryItem> = emptyList()
)

data class SocialLink(val name: String, val url: String)

data class GigHistoryItem(
    val id: String,
    val month: String,
    val day: String,
    val title: String,
    val location: String,
    val role: String
)
