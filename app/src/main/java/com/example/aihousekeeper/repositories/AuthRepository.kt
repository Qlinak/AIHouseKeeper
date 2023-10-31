package com.example.aihousekeeper.repositories

import android.util.Log
import com.example.aihousekeeper.datas.ValidateUsernameRequest
import com.example.aihousekeeper.utils.APIConsumer
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.flow.flow

class AuthRepository(private val consumer: APIConsumer) {
    fun validateUsername(body: ValidateUsernameRequest) = flow {
        emit(RequestStatus.Waiting)
        val response  = consumer.validateUsername(body)
        if(response.isSuccessful){
            Log.d("BACKEND", response.body().toString())
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error(response.errorBody().toString()))
        }
    }
}