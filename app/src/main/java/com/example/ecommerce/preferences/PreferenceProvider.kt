package com.example.ecommerce.preferences


import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceProvider @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun saveOnBoardingStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_onboardinig_completed", true)
        editor.apply()
    }

    fun saveAccess(accessKey : String, refreshKey : String){
        val editor = sharedPreferences.edit()
        editor.putString("api_access_key", accessKey)
        editor.putString("api_refresh_key", refreshKey)
        editor.apply()

    }

    fun getApiAccessKey(): String? {
        return sharedPreferences.getString("api_access_key", null)
    }

    fun getRefreshKey() : String? {
        return sharedPreferences.getString("api_refresh_key", null)
    }

    fun isOnBoardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("is_onboardinig_completed", false)
    }
}