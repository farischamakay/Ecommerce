package com.example.ecommerce.main.home

import androidx.lifecycle.ViewModel
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferenceManager: PreferenceProvider
) : ViewModel() {

    fun saveTheme(isChecked: Boolean) {
        return sharedPreferenceManager.saveDarkTheme(isChecked)
    }

    fun isDarkThemeMode(): Boolean {
        return sharedPreferenceManager.isDarkTheme()
    }
}