package com.example.ecommerce.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferenceProvider (context : Context) {

    private val sharedPreferences : SharedPreferences = context.getSharedPreferences(
        "onboarding_pref", Context.MODE_PRIVATE)

    fun saveOnBoardingStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_onboardinig_completed", true)
        editor.apply()
    }

    fun isOnBoardingCompleted() : Boolean {
        return sharedPreferences.getBoolean("is_onboardinig_completed", false)
    }
}