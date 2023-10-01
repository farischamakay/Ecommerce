package com.example.ecommerce.main.home

import com.example.ecommerce.preferences.PreferenceProvider
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class HomeViewModelTest {

    private lateinit var sharedPreferenceManager : PreferenceProvider
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        sharedPreferenceManager = mock()
        homeViewModel = HomeViewModel(sharedPreferenceManager)
    }

    @Test
    fun deleteToken() {
        doNothing(). whenever(sharedPreferenceManager).deleteTokenAccess()
        assertEquals(Unit, homeViewModel.deleteToken())
    }

    @Test
    fun saveTheme() {
        doNothing().whenever(sharedPreferenceManager).saveDarkTheme(true)
        assertEquals(Unit, homeViewModel.saveTheme(true))
    }

    @Test
    fun isDarkThemeMode() {
        val expectedTheme = true
        whenever(sharedPreferenceManager.isDarkTheme()).thenReturn(expectedTheme)
        assertEquals(expectedTheme, homeViewModel.isDarkThemeMode())
    }
}