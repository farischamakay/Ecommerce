package com.example.ecommerce.prelogin.onboarding

import androidx.lifecycle.ViewModel
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnboardingViewModel @Inject constructor(private val sharedPreferenceManager: PreferenceProvider) :
    ViewModel() {
    fun markOnboardingCompleted() {
        sharedPreferenceManager.saveOnBoardingStatus()
    }

    fun isOnboardingCompleted(): Boolean {
        return sharedPreferenceManager.isOnBoardingCompleted()
    }
}