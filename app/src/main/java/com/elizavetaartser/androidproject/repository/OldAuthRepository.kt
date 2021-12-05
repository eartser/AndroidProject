package com.elizavetaartser.androidproject.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object OldAuthRepository {

    private val _isAuthorizedFlow = MutableStateFlow(false)
    val isAuthorizedFlow = _isAuthorizedFlow.asStateFlow()

    suspend fun signIn(email: String, password: String) {
        _isAuthorizedFlow.emit(true)
    }

    suspend fun logout() {
        _isAuthorizedFlow.emit(false)
    }

    suspend fun signUp(
        firstname: String,
        lastname: String,
        login: String,
        email: String,
        password: String
    ) {
        //TODO: Get API response for email availability, change screen to email confirm
    }
}