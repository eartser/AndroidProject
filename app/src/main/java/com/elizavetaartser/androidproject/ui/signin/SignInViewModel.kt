package com.elizavetaartser.androidproject.ui.signin

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.repository.AuthRepository
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel() {

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            AuthRepository.signIn(email, password)
        }
    }
}