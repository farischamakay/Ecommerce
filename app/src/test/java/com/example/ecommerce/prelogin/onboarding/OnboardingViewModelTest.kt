package com.example.ecommerce.prelogin.onboarding

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.preferences.PreferenceProvider
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class OnboardingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var sharedPreferences: PreferenceProvider
    private lateinit var onboardingViewModel: OnboardingViewModel

    @Before
    fun setUp() {
        sharedPreferences = mock()
        onboardingViewModel = OnboardingViewModel(sharedPreferences)
    }

    @Test
    fun markOnboardingCompleted() {
        doNothing().whenever(sharedPreferences).saveOnBoardingStatus()
        onboardingViewModel.markOnboardingCompleted()
        verify(sharedPreferences).saveOnBoardingStatus()
    }

    @Test
    fun isOnboardingCompleted() {
        val expectedStatus = true
        whenever(sharedPreferences.isOnBoardingCompleted()).thenReturn(expectedStatus)
        val result = onboardingViewModel.isOnboardingCompleted()
        assertEquals(expectedStatus, result)
    }
}