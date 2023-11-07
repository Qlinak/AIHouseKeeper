package com.example.aihousekeeper.view_models

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aihousekeeper.repositories.AiRepository

@Suppress("UNCHECKED_CAST")
class HomeActivityViewModelFactory(private val aiRepository: AiRepository, private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeActivityViewModel::class.java) -> {
                HomeActivityViewModel(aiRepository, application) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.simpleName}")
            }
        }
    }
}