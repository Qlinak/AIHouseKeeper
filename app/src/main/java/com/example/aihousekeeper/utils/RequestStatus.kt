package com.example.aihousekeeper.utils

sealed class RequestStatus<out T> {
    object Waiting: RequestStatus<Nothing>()
    data class Success<out T>(val data: T): RequestStatus<T>()
    data class Error(val message: String): RequestStatus<Nothing>()
}