package com.example.aihousekeeper.view_models

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aihousekeeper.datas.LoginUserRequest
import com.example.aihousekeeper.repositories.AuthRepository
import com.example.aihousekeeper.utils.AuthToken
import com.example.aihousekeeper.utils.RequestStatus
import kotlinx.coroutines.launch

class LoginActivityViewModel(private val authRepository: AuthRepository, val application: Application): ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<String> = MutableLiveData()
    private var isLoginCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = AuthToken.getInstance(application.baseContext).token != null }

    fun getIsLoading() = isLoading
    fun getErrorMessage() = errorMessage
    fun getIsLoginCompleted() = isLoginCompleted

    fun loginUser(body: LoginUserRequest){
        viewModelScope.launch {
            authRepository.loginUser(body).collect{
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isLoginCompleted.value = true
                        AuthToken.getInstance(application.baseContext).token = it.data!!.token
                        AuthToken.getInstance(application.baseContext).userId = it.data.id.toString()
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