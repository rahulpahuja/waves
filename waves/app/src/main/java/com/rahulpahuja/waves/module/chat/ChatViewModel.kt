package com.rahulpahuja.waves.module.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulpahuja.waves.data.remote.FirestoreMessage
import com.rahulpahuja.waves.data.remote.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    private val chatId = "global_chat" // Example chatId

    init {
        observeMessages()
    }

    private fun observeMessages() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                contactName = "Community Chat",
                contactRole = "Group"
            )
            repository.getMessages(chatId).collect { firestoreMessages ->
                val messages = firestoreMessages.map { it.toMessage() }
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    private fun FirestoreMessage.toMessage(): Message {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return Message(
            id = timestamp.toString(),
            content = content,
            timestamp = sdf.format(Date(timestamp)),
            type = when (type) {
                "audio" -> MessageType.AUDIO
                "image" -> MessageType.IMAGE
                else -> MessageType.TEXT
            },
            isMe = senderId == "me" // Replace with actual current user ID
        )
    }

    fun onMessageInputChange(input: String) {
        _messageInput.value = input
    }

    fun sendMessage() {
        if (_messageInput.value.isNotBlank()) {
            val content = _messageInput.value
            _messageInput.value = ""
            viewModelScope.launch {
                val firestoreMessage = FirestoreMessage(
                    senderId = "me", // Replace with actual current user ID
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    type = "text"
                )
                repository.sendMessage(chatId, firestoreMessage)
            }
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
