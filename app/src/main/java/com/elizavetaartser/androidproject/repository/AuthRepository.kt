package com.elizavetaartser.androidproject.repository

import com.elizavetaartser.androidproject.data.network.Api
import com.elizavetaartser.androidproject.data.network.request.CreateProfileRequest
import com.elizavetaartser.androidproject.data.network.request.RefreshAuthTokensRequest
import com.elizavetaartser.androidproject.data.network.request.SignInWithEmailRequest
import com.elizavetaartser.androidproject.data.network.response.error.CreateProfileErrorResponse
import com.elizavetaartser.androidproject.data.network.response.error.RefreshAuthTokensErrorResponse
import com.elizavetaartser.androidproject.data.network.response.error.SignInWithEmailErrorResponse
import com.elizavetaartser.androidproject.data.persistent.LocalKeyValueStorage
import com.elizavetaartser.androidproject.di.AppCoroutineScope
import com.elizavetaartser.androidproject.di.IoCoroutineDispatcher
import com.elizavetaartser.androidproject.entity.AuthTokens
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.Lazy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiLazy: Lazy<Api>,
    private val localKeyValueStorage: LocalKeyValueStorage,
    @AppCoroutineScope externalCoroutineScope: CoroutineScope,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val api by lazy { apiLazy.get() }

    private val authTokensFlow: Deferred<MutableStateFlow<AuthTokens?>> =
        externalCoroutineScope.async(context = ioDispatcher, start = CoroutineStart.LAZY) {
            Timber.d("Initializing auth tokens flow.")
            MutableStateFlow(
                localKeyValueStorage.authTokens
            )
        }

    suspend fun getAuthTokensFlow(): StateFlow<AuthTokens?> {
        return authTokensFlow.await().asStateFlow()
    }

    /**
     * @param authTokens active auth tokens which must be used for signing all requests
     */
    suspend fun saveAuthTokens(authTokens: AuthTokens?) {
        withContext(ioDispatcher) {
            Timber.d("Persist auth tokens $authTokens.")
            localKeyValueStorage.authTokens = authTokens
        }
        Timber.d("Emit auth tokens $authTokens.")
        authTokensFlow.await().emit(authTokens)
    }

    /**
     * @return whether active access tokens are authorized or not
     */
    suspend fun isAuthorizedFlow(): Flow<Boolean> {
        return authTokensFlow
            .await()
            .asStateFlow()
            .map { it != null }
    }

    suspend fun generateAuthTokensByEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        return api.signInWithEmail(SignInWithEmailRequest(email, password))
    }

    /**
     * Creates a user account in the system as a side effect.
     * @return access tokens with higher permissions for the new registered user
     */
    suspend fun generateAuthTokensByEmailAndPersonalInfo(
        verificationToken: String,
        firstName: String,
        lastName: String,
        email: String,
        login: String,
        password: String
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        return api.createProfile(
            CreateProfileRequest(
                verificationToken,
                firstName,
                lastName,
                email,
                login,
                password
            )
        )
    }

    suspend fun generateRefreshedAuthTokens(refreshToken: String): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        return api.refreshAuthTokens(RefreshAuthTokensRequest(refreshToken))
    }
}