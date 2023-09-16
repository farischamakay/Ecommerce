package com.example.ecommerce.preferences


import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceProvider @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveOnBoardingStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_onboardinig_completed", true)
        editor.apply()
    }

    fun saveDarkTheme(isChecked: Boolean) {
        val dark = sharedPreferences.edit()
        dark.putBoolean("dark_theme_on", isChecked)
        dark.apply()
    }

    fun saveAccess(accessKey: String, refreshKey: String) {
        val editor = sharedPreferences.edit()
        editor.putString("api_access_key", accessKey)
        editor.putString("api_refresh_key", refreshKey)
        editor.apply()
    }

    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString("userName", username)
        editor.apply()
    }

    fun deleteTokenAccess() {
        val editor = sharedPreferences.edit()
        editor.remove("api_access_key")
        editor.remove("api_refresh_key")
        editor.remove("userName")
        editor.apply()
    }

    fun getApiAccessKey(): String? {
        return sharedPreferences.getString("api_access_key", null)
    }

    fun getRefreshKey(): String? {
        return sharedPreferences.getString("api_refresh_key", null)
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("userName", null)
    }

    fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean("dark_theme_on", false)
    }


    fun isOnBoardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("is_onboardinig_completed", false)
    }
}