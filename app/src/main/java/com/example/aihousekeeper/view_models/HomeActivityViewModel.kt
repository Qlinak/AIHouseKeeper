package com.example.aihousekeeper.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aihousekeeper.datas.PromptRequest
import com.example.aihousekeeper.repositories.AiRepository
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.launch

class HomeActivityViewModel(private val aiRepository: AiRepository, val application: Application): ViewModel(){
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
    private var displayMessage: MutableLiveData<String> = MutableLiveData()

    fun getIsLoading() = isLoading
    fun getErrorMessage() = errorMessage
    fun getDisplayMessage() = displayMessage

    fun askAi(body: PromptRequest){
        viewModelScope.launch {
            aiRepository.askAi(body).collect{
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                        displayMessage.value = "Waiting..."
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        displayMessage.value = it.data!!.message
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
}