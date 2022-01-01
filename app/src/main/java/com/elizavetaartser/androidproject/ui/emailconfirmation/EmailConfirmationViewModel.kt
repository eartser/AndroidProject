package com.elizavetaartser.androidproject.ui.emailconfirmation

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.data.network.response.VerificationTokenResponse
import com.elizavetaartser.androidproject.data.network.response.error.SendRegistrationVerificationCodeErrorResponse
import com.elizavetaartser.androidproject.data.network.response.error.VerifyRegistrationCodeErrorResponse
import com.elizavetaartser.androidproject.interactor.AuthInteractor
import com.elizavetaartser.androidproject.interactor.EmailConfirmationInteractor
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val emailConfirmationInteractor: EmailConfirmationInteractor
): BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authInteractor.signInWithEmail(email, password)
        }
    }

    fun sendVerificationCode(email: String) {
        viewModelScope.launch {
            _eventChannel.send(
                try {
                    when (val response = emailConfirmationInteractor.sendVerificationCode(email)) {
                        is NetworkResponse.Success -> Event.VerificationCodeSent
                        is NetworkResponse.ServerError -> Event.VerificationCodeSendError(response)
                        is NetworkResponse.NetworkError -> Event.NetworkError(response)
                        is NetworkResponse.UnknownError -> Event.UnknownError(response)
                    }
                } catch (error: Throwable) {
                    Timber.e(error)
                    Event.UnknownError(NetworkResponse.UnknownError(error))
                }
            )
        }
    }

    fun verifyCode(code: String, email: String) {
        viewModelScope.launch {
            _eventChannel.send(
                try {
                    when (val response = emailConfirmationInteractor.verifyCode(code, email)) {
                        is NetworkResponse.Success -> Event.VerificationSuccess(response.body)
                        is NetworkResponse.ServerError -> Event.VerificationFailed(response)
                        is NetworkResponse.NetworkError -> Event.NetworkError(response)
                        is NetworkResponse.UnknownError -> Event.UnknownError(response)
                    }
                } catch (error: Throwable) {
                    Timber.e(error)
                    Event.UnknownError(NetworkResponse.UnknownError(error))
                }
            )
        }
    }

    sealed class Event {
        object VerificationCodeSent: Event()
        data class VerificationCodeSendError(
            val e: NetworkResponse.ServerError<SendRegistrationVerificationCodeErrorResponse>
        ) : Event()
        data class NetworkError(val e: NetworkResponse.NetworkError) : Event()
        data class UnknownError(val e: NetworkResponse.UnknownError) : Event()
        data class VerificationSuccess(val token: VerificationTokenResponse) : Event()
        data class VerificationFailed(
            val e: NetworkResponse.ServerError<VerifyRegistrationCodeErrorResponse>
        ) : Event()
    }

}
