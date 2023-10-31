package com.example.aihousekeeper.repositories

import com.example.aihousekeeper.datas.RegisterUserRequest
import com.example.aihousekeeper.datas.ValidateEmailRequest
import com.example.aihousekeeper.datas.ValidateUsernameRequest
import com.example.aihousekeeper.utils.APIConsumer
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class AuthRepository(private val consumer: APIConsumer) {
    fun validateUsername(body: ValidateUsernameRequest) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateUsername(body)
        if(response.isSuccessful){
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error(response.errorBody().toString()))
        }
    }

    fun validateUserEmail(body: ValidateEmailRequest) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateUserEmail(body)
        if(response.isSuccessful){
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error(response.errorBody().toString()))
        }
    }

    fun registerUser(body: RegisterUserRequest) = flow {
        emit(RequestStatus.Waiting)
        val response: Response<Void> = consumer.registerUser(body)
        if(response.isSuccessful){
            emit(RequestStatus.Success(null))
        }
        else{
            emit(RequestStatus.Error(response.errorBody().toString()))
        }
    }
}