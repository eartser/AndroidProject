package com.elizavetaartser.androidproject.data.network
import com.elizavetaartser.androidproject.entity.User
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