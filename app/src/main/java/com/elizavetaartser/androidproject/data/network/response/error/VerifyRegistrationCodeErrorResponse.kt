package com.elizavetaartser.androidproject.data.network.response.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VerifyRegistrationCodeErrorResponse(
    @Json(name = "non_field_errors") val nonFieldErrors: List<Error>?
)