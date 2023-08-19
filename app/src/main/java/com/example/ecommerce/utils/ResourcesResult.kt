package com.example.ecommerce.utils

sealed class ResourcesResult<out T> {
    object Loading: ResourcesResult<Nothing>()
    data class  Success<out T>(val data:T): ResourcesResult<T>()
    data class Failure(val error: String?): ResourcesResult<Nothing>()
}
