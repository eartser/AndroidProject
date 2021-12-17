package com.elizavetaartser.androidproject.ui.profile

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.data.network.Api
import com.elizavetaartser.androidproject.entity.User
import com.elizavetaartser.androidproject.interactor.AuthInteractor
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val api: Api
): BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)
    var user: User? = null

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authInteractor.logout()
            } catch (error: Throwable) {
                Timber.e(error)
                _eventChannel.send(Event.LogoutError(error))
            }
        }
    }

    init {
        viewModelScope.launch {
            user = loadProfile()
            _eventChannel.send(Event.ProfileLoaded)
        }
    }

    private suspend fun loadProfile(): User {
        return api.getProfile()
    }

    sealed class Event {
        data class LogoutError(val error: Throwable) : Event()
        object ProfileLoaded : Event()
    }
}
