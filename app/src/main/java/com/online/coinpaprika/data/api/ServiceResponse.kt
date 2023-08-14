package com.online.coinpaprika.data.api
sealed class ServiceResponse<out T>{

    data class Success<out T : Any>(val data: T) : ServiceResponse<T>()
    data class Error(val message: String) : ServiceResponse<Nothing>()
    object Loading: ServiceResponse<Nothing>()
}
