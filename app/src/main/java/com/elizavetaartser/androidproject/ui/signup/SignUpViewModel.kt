package com.elizavetaartser.androidproject.ui.signup

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.interactor.AuthInteractor
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

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
                // authInteractor.signInWithEmail(email, password)
                throw RuntimeException("TODO: implement signing up")
                _eventChannel.send(Event.SignUpSuccess)
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