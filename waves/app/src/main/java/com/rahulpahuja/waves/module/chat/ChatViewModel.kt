package com.rahulpahuja.waves.module.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    init {
        loadMessages()
    }

    private fun loadMessages() {
        viewModelScope.launch {
            _uiState.value = ChatUiState(
                contactName = "DJ Snake",
                contactRole = "Instructor",
                messages = listOf(
                    Message("1", "on that House track.", "10:30 AM", MessageType.TEXT, false),
                    Message("2", "Feedback_Mix_v2.mp3", "10:31 AM", MessageType.AUDIO, false, duration = "0:45"),
                    Message("3", "Thanks! I was struggling with the EQ there.", "10:35 AM", MessageType.TEXT, true),
                    Message("4", "", "10:36 AM", MessageType.IMAGE, true, imageUrl = "placeholder"), // In real app, use actual URL
                    Message("5", "Is this curve too aggressive?", "10:36 AM", MessageType.TEXT, true),
                    Message("6", "A little bit. Try cutting the lows on the incoming track earlier.", "10:42 AM", MessageType.TEXT, false)
                )
            )
        }
    }

    fun onMessageInputChange(input: String) {
        _messageInput.value = input
    }

    fun sendMessage() {
        if (_messageInput.value.isNotBlank()) {
            val newMessage = Message(
                id = System.currentTimeMillis().toString(),
                content = _messageInput.value,
                timestamp = "Now",
                type = MessageType.TEXT,
                isMe = true
            )
            val updatedMessages = _uiState.value.messages + newMessage
            _uiState.value = _uiState.value.copy(messages = updatedMessages)
            _messageInput.value = ""
        }
    }
}

data class ChatUiState(
    val contactName: String = "",
    val contactRole: String = "",
    val messages: List<Message> = emptyList()
)

data class Message(
    val id: String,
    val content: String,
    val timestamp: String,
    val type: MessageType,
    val isMe: Boolean,
    val duration: String? = null,
    val imageUrl: String? = null
)

enum class MessageType { TEXT, AUDIO, IMAGE }
