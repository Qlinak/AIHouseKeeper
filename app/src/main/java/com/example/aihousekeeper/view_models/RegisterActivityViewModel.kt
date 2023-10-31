package com.example.aihousekeeper.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aihousekeeper.datas.ValidateEmailRequest
import com.example.aihousekeeper.datas.ValidateUsernameRequest
import com.example.aihousekeeper.repositories.AuthRepository
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.launch

class RegisterActivityViewModel(val authRepository: AuthRepository, val application: Application): ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
    private var isUsernameUnique: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var isEmailUnique: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }

    fun getIsLoading() = isLoading
    fun getErrorMessage() = errorMessage
    fun getIsUsernameUnique() = isUsernameUnique
    fun getIsEmailUnique() = isEmailUnique

    fun validateUsername(body: ValidateUsernameRequest){
        viewModelScope.launch {
            authRepository.validateUsername(body).collect{
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isUsernameUnique.value = it.data!!.isUnique
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }

    fun validateUserEmail(body: ValidateEmailRequest){
        viewModelScope.launch {
            authRepository.validateUserEmail(body).collect{
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isEmailUnique.value = it.data!!.isUnique
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