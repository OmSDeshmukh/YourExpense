package com.example.expensetracker.api

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.StateFlow

sealed class NetworkResponse <out T> {
    data class Success<out T>(val data: T? = null): NetworkResponse<T>()
    data class Error(val message : String) : NetworkResponse<Nothing>()
    data object Loading : NetworkResponse<Nothing>()
}