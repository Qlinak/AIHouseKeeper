package com.example.aihousekeeper.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.aihousekeeper.repositories.AiRepository
import com.example.aihousekeeper.repositories.AuthRepository

class HomeActivityViewModel(private val aiRepository: AiRepository, val application: Application): ViewModel(){
    fun askAi(){

    }


}