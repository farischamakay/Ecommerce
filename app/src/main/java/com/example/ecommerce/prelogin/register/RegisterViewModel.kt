package com.example.ecommerce.prelogin.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.request.UserRequest
import com.example.ecommerce.data.models.response.RegisterResponse
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferencesManager : PreferenceProvider
) :
    ViewModel() {

    private val _registerResult = MutableLiveData<ResourcesResult<RegisterResponse>>()
    val registerResult: LiveData<ResourcesResult<RegisterResponse>> = _registerResult

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            _registerResult.value = ResourcesResult.Loading
            val result = userRepository.registerUser(userRequest)
            _registerResult.value = result
        }
    }

    fun saveToken(accessToken : String, refreshToken : String){
        sharedPreferencesManager.saveAccess(accessToken,refreshToken)
    }

}