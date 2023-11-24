package com.example.aihousekeeper.repositories

import android.app.Application
import com.example.aihousekeeper.datas.PromptRequest
import com.example.aihousekeeper.utils.APIConsumer
import com.example.aihousekeeper.utils.AuthToken
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.flow.flow

class AiRepository(private val consumer: APIConsumer, val application: Application) {
    fun askAi(body: PromptRequest) = flow{
        emit(RequestStatus.Waiting)
        val response = consumer.askAi(
            AuthToken.getInstance(application.baseContext).token!!,
            body)
        if(response.isSuccessful){
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error("something went wrong..."))
        }
    }

    fun getMemory() = flow{
        emit(RequestStatus.Waiting)
        val response = consumer.getMemory(
            AuthToken.getInstance(application.baseContext).token!!,
            AuthToken.getInstance(application.baseContext).userId!!)
        if(response.isSuccessful){
            emit(RequestStatus.Success(response.body()))
        }
        else{
            emit(RequestStatus.Error("something went wrong..."))
        }
    }
}