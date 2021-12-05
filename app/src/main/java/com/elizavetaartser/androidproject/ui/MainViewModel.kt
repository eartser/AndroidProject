package com.elizavetaartser.androidproject.ui

import com.elizavetaartser.androidproject.interactor.AuthInteractor
import com.elizavetaartser.androidproject.repository.OldAuthRepository
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authInteractor : AuthInteractor
): BaseViewModel() {

    suspend fun isAuthorizedFlow(): Flow<Boolean> =
        authInteractor.isAuthorized()

}