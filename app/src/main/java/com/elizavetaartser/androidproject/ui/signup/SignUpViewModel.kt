package com.elizavetaartser.androidproject.ui.signup

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.repository.OldAuthRepository
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel : BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun signUp(
        firstname: String,
        lastname: String,
        login: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                OldAuthRepository.signUp(
                    firstname,
                    lastname,
                    login,
                    email,
                    password
                )
                // _eventChannel.send(Event.SignUpSuccess)
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (error: Exception) {
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            }
        }
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
    }
}