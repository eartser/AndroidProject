package com.elizavetaartser.androidproject.ui

import com.elizavetaartser.androidproject.repository.AuthRepository
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : BaseViewModel() {

    val isAuthorizedFlow: Flow<Boolean> = AuthRepository.isAuthorizedFlow
}