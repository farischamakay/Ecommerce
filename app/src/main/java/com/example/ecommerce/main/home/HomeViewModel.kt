package com.example.ecommerce.main.home

import androidx.lifecycle.ViewModel
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferenceManager: PreferenceProvider
) : ViewModel() {
    fun getUserNameLogin(): String? {
        return sharedPreferenceManager.getUsername()
    }

    fun deleteToken() {
        return sharedPreferenceManager.deleteTokenAccess()
    }
}