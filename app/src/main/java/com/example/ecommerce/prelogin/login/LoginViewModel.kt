package com.example.ecommerce.prelogin.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.LoginResponse
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedUserPreferenceManager: PreferenceProvider
) : ViewModel() {

    private val _loginResult = MutableLiveData<ResourcesResult<LoginResponse>>()
    val loginResult: LiveData<ResourcesResult<LoginResponse>> = _loginResult

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            _loginResult.value = ResourcesResult.Loading
            val result = userRepository.loginUser(userRequest)
            _loginResult.value = result
        }
    }

    fun saveToken(accessKey: String, refreshKey: String) {
        return sharedUserPreferenceManager.saveAccess(accessKey, refreshKey)
    }

    fun saveUserName(username: String) {
        return sharedUserPreferenceManager.saveUsername(username)
    }

    fun getUsername(): String? {
        return sharedUserPreferenceManager.getUsername()
    }
}