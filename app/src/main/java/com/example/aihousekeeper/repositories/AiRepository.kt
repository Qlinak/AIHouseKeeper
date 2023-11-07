package com.example.aihousekeeper.repositories

import com.example.aihousekeeper.datas.PromptRequest
import com.example.aihousekeeper.utils.APIConsumer
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.flow.flow

class AiRepository(private val consumer: APIConsumer) {
    fun askAi(body: PromptRequest) = flow{
        emit(RequestStatus.Waiting)
        val response = consumer.askAi(body)
        if(response.isSuccessful){
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error(response.errorBody().toString()))
        }
    }
}