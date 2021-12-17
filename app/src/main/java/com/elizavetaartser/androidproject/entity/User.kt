package com.elizavetaartser.androidproject.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "user_name") val login: String,
    @Json(name = "first_name") val first_name: String,
    @Json(name = "last_name") val last_name: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "picture") val avatar: String?,
    @Json(name = "about_me") val group_name: String?,
    @Json(name = "phone_number") val phone_number: String?
)
