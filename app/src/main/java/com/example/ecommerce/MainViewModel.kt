package com.example.ecommerce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.repository.NotificationRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomCartRepository: RoomCartRepository,
    private val sharedPreferencesManager: PreferenceProvider,
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val getDataWishlist =
        roomCartRepository.fetchWishlistData()

    val getDataRoom =
        roomCartRepository.fetchCartData()

    val getDataNotification = notificationRepository.fetchDataNotification()

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }

    fun isDarkModeTheme(): Boolean {
        return sharedPreferencesManager.isDarkTheme()
    }

    fun deleteCheckedItems() {
        viewModelScope.launch {
            roomCartRepository.deleteData()
        }
    }
}