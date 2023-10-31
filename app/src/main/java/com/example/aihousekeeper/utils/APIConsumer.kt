package com.example.aihousekeeper.utils

import com.example.aihousekeeper.datas.ValidateUsernameRequest
import com.example.aihousekeeper.datas.ValidateUsernameResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("Authorisation/ValidateUsername")
    suspend fun validateUsername(@Body body: ValidateUsernameRequest) : Response<ValidateUsernameResponse>
}