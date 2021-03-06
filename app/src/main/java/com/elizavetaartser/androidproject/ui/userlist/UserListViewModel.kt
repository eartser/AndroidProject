package com.elizavetaartser.androidproject.ui.userlist

import androidx.lifecycle.viewModelScope
import com.elizavetaartser.androidproject.BuildConfig
import com.elizavetaartser.androidproject.data.network.Api
import com.elizavetaartser.androidproject.data.network.MockApi
import com.elizavetaartser.androidproject.entity.User
import com.elizavetaartser.androidproject.ui.base.BaseViewModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class UserListViewModel : BaseViewModel() {

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val userlist: List<User>) : ViewState()
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            _viewState.emit(ViewState.Loading)
            val users = loadUsers()
            _viewState.emit(ViewState.Data(users))
        }
    }

    private suspend fun loadUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            Thread.sleep(3000)
            provideApi().getUsers().data
        }
    }

    private fun provideApi(): Api =
        if (BuildConfig.USE_MOCK_BACKEND_API) {
            MockApi()
        }
        else {
            Retrofit.Builder()
                .client(provideOkHttpClient())
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(MoshiConverterFactory.create(provideMoshi()))
                .build()
                .create(Api::class.java)
        }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // .addNetworkInterceptor(AuthorizationInterceptor(authRepository))
            .build()
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

}