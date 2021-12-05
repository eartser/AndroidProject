package com.elizavetaartser.androidproject
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET

interface Api {

    @GET("users?per_page=10")
    suspend fun getUsers(): GetUserResponse
}

@JsonClass(generateAdapter = true)
data class GetUserResponse(
    @Json(name = "data") val data: List<User>
)