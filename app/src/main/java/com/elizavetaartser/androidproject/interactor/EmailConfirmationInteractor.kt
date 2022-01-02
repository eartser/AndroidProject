package com.elizavetaartser.androidproject.interactor

import com.elizavetaartser.androidproject.data.network.Api
import com.elizavetaartser.androidproject.data.network.response.VerificationTokenResponse
import com.elizavetaartser.androidproject.data.network.response.error.SendRegistrationVerificationCodeErrorResponse
import com.elizavetaartser.androidproject.data.network.response.error.VerifyRegistrationCodeErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class EmailConfirmationInteractor @Inject constructor(
    private val api: Api
) {
    suspend fun sendVerificationCode(email: String)
            : NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> =
        api.sendRegistrationVerificationCode(email)

    suspend fun verifyCode(code: String, email: String)
            : NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> =
        api.verifyRegistrationCode(code, email, null)
}