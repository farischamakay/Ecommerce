package com.example.ecommerce

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.repository.NotificationRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    roomCartRepository: RoomCartRepository,
    notificationRepository: NotificationRepository,
    private val sharedPreferencesManager: PreferenceProvider,
    private val sessionManager: SessionManager
) : ViewModel() {

    val getDataWishlist =
        roomCartRepository.fetchWishlistData()

    val getDataRoom =
        roomCartRepository.fetchCartData()

    val getDataNotification = notificationRepository.fetchDataNotification()

    val sessionExpired: LiveData<Boolean?> = sessionManager.tokenExpired

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }

    fun deleteToken() {
        return sharedPreferencesManager.deleteTokenAccess()
    }

    fun isDarkModeTheme(): Boolean {
        return sharedPreferencesManager.isDarkTheme()
    }

    fun resetSession() {
        sessionManager.resetSession()
    }
}