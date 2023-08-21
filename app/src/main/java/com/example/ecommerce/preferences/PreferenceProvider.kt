package com.example.ecommerce.preferences

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceProvider @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveOnBoardingStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_onboardinig_completed", true)
        editor.apply()
    }

    fun isOnBoardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("is_onboardinig_completed", false)
    }
}