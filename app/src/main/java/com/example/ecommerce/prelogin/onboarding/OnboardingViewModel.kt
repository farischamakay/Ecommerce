package com.example.ecommerce.prelogin.onboarding

import androidx.lifecycle.ViewModel
import com.example.ecommerce.preferences.PreferenceProvider

class OnboardingViewModel : ViewModel (){
    fun markOnboardingCompleted(sharedPreferenceManager: PreferenceProvider){
        sharedPreferenceManager.saveOnBoardingStatus()
    }
    fun isOnboardingCompleted(preferenceManager: PreferenceProvider): Boolean {
        return preferenceManager.isOnBoardingCompleted() // Implement this based on your preference storage logic
    }
}