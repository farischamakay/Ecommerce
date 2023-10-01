package com.example.ecommerce.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PreferenceProviderTest {

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var preferenceProvider: PreferenceProvider
    private val preferenceNameFile = "PreferenceProvider"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferences = context.getSharedPreferences(preferenceNameFile, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        preferenceProvider = PreferenceProvider(sharedPreferences)
    }
    @Test
    fun saveOnBoardingStatus() {
        preferenceProvider.saveOnBoardingStatus()
        assertEquals(true, preferenceProvider.isOnBoardingCompleted())
    }

    @Test
    fun saveDarkTheme() {
        val isChecked = true
        preferenceProvider.saveDarkTheme(isChecked)
        assertEquals(isChecked, preferenceProvider.isDarkTheme())
    }

    @Test
    fun saveAccess() {
        val accessKey = "test_access_key"
        val refreshKey = "test_refresh_key"
        preferenceProvider.saveAccess(accessKey, refreshKey)
        assertEquals(accessKey, preferenceProvider.getApiAccessKey())
        assertEquals(refreshKey, preferenceProvider.getRefreshKey())
    }

    @Test
    fun saveUsername() {
        val username = "test_username"
        preferenceProvider.saveUsername(username)
        assertEquals(username, preferenceProvider.getUsername())
    }

    @Test
    fun deleteTokenAccess() {
        val apiAccessKey = "api_access_key"
        val apiRefreshKey = "api_refresh_key"
        val userName = "userName"
        preferenceProvider.saveAccess(apiAccessKey,apiRefreshKey)
        preferenceProvider.saveUsername(userName)
        preferenceProvider.deleteTokenAccess()

//        assertEquals(empt)

//        assertTrue(null, preferenceProvider.saveAccess(apiAccessKey,apiRefreshKey).equals(null))
//        assertTrue(null, preferenceProvider.saveUsername(userName).equals(null))
//        assertTrue(null, preferenceProvider.deleteTokenAccess().equals(null))
    }

    @Test
    fun getApiAccessKey() {
        val accessKey = "api_access_key"
        preferenceProvider.saveAccess(accessKey, "api_access_key")
        assertEquals(accessKey, preferenceProvider.getApiAccessKey())
    }

    @Test
    fun getRefreshKey() {
        val refreshKey = "api_refresh_key"
        preferenceProvider.saveAccess(refreshKey, "api_refresh_key")
        assertEquals(refreshKey, preferenceProvider.getRefreshKey())
    }
    @Test
    fun getUsername() {
        val username = "name"
        preferenceProvider.saveUsername(username)
        assertEquals(username, preferenceProvider.getUsername())
    }

    @Test
    fun isDarkTheme(){
        val isChecked = true
        preferenceProvider.saveDarkTheme(isChecked)
        assertEquals(isChecked, preferenceProvider.isDarkTheme())
    }

    @Test
    fun isOnBoardingCompleted() {
        val isOnBoarding = true
        preferenceProvider.saveOnBoardingStatus()
        assertEquals(isOnBoarding, preferenceProvider.isOnBoardingCompleted())
    }
}