package com.elizavetaartser.androidproject.ui.emailconfirmation

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.interactor.AuthInteractor
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailConfirmationViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
): BaseViewModel() {

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            authInteractor.signInWithEmail(email, password)
        }
    }

}
