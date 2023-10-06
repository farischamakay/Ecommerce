package com.example.ecommerce.core.data.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    val _tokenExpired = MutableLiveData(false)
    val tokenExpired: LiveData<Boolean?> = _tokenExpired

    fun setTokenExpired() {
        _tokenExpired.postValue(true)
    }

    fun resetSession() {
        _tokenExpired.postValue(null)
    }
}