package com.example.ecommerce.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.data.models.request.ProfileRequest
import com.example.ecommerce.core.data.models.response.ProfileResponse
import com.example.ecommerce.core.data.preferences.PreferenceProvider
import com.example.ecommerce.data.repository.UserRepository
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private var sharedPreferencesManager: PreferenceProvider
) : ViewModel() {

    private val _profileResult = MutableLiveData<ResourcesResult<ProfileResponse?>?>()
    val profileResult: MutableLiveData<ResourcesResult<ProfileResponse?>?> = _profileResult

    fun updateProfile(profileRequest: ProfileRequest?) {
        viewModelScope.launch {
            _profileResult.value = ResourcesResult.Loading
            val result = profileRequest?.let { userRepository.profileUser(it) }
            _profileResult.value = result
        }
    }

    fun saveUserName(username: String) {
        return sharedPreferencesManager.saveUsername(username)
    }

}