package com.example.aihousekeeper.utils

import com.example.aihousekeeper.datas.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {
    @POST("Authorisation/ValidateUsername")
    suspend fun validateUsername(@Body body: ValidateUsernameRequest) : Response<ValidateUsernameResponse>

    @POST("Authorisation/ValidateUserEmail")
    suspend fun validateUserEmail(@Body body: ValidateEmailRequest) : Response<ValidateEmailResponse>

    @POST("Authorisation/SignUp")
    suspend fun registerUser(@Body body: RegisterUserRequest) : Response<Void>
}