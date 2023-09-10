package com.example.ecommerce

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val roomCartRepository: RoomCartRepository,
    private val sharedPreferencesManager: PreferenceProvider) : ViewModel(){

    val getDataWishlist =
        roomCartRepository.fetchWishlistData()

    val getDataRoom =
        roomCartRepository.fetchCartData()

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }
}