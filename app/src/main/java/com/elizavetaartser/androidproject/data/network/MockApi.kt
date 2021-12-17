package com.elizavetaartser.androidproject.data.network

import com.elizavetaartser.androidproject.data.network.request.CreateProfileRequest
import com.elizavetaartser.androidproject.data.network.request.RefreshAuthTokensRequest
import com.elizavetaartser.androidproject.data.network.request.SignInWithEmailRequest
import com.elizavetaartser.androidproject.data.network.response.VerificationTokenResponse
import com.elizavetaartser.androidproject.data.network.response.error.*
import com.elizavetaartser.androidproject.entity.AuthTokens
import com.elizavetaartser.androidproject.entity.User
import com.haroldadmin.cnradapter.NetworkResponse

class MockApi : Api {
    override suspend fun getUsers(): GetUsersResponse {
        return GetUsersResponse(
            (0 until 20).map {
                User(
                    "https://d2ph5fj80uercy.cloudfront.net/05/cat${1000 + it}.jpg",
                    "User $it",
                    "Group A"
                )
            }
        )
    }

    override suspend fun signInWithEmail(request: SignInWithEmailRequest): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        return NetworkResponse.Success(
            AuthTokens(
                accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                accessTokenExpiration = 1640871771000,
                refreshTokenExpiration = 1640871771000,
            ),
            code = 200
        )
    }

    override suspend fun refreshAuthTokens(request: RefreshAuthTokensRequest): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRegistrationVerificationCode(email: String): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyRegistrationCode(
        code: String,
        email: String?,
        phoneNumber: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createProfile(request: CreateProfileRequest): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        TODO("Not yet implemented")
    }
}