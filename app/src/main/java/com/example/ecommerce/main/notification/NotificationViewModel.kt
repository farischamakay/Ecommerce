package com.example.ecommerce.main.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.database.notification.Notification
import com.example.ecommerce.data.repository.NotificationRepository
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel(){
//
//    private val _notifData = MutableLiveData<Notification>()
//    val notifData : LiveData<Notification> = _notifData
//
//    fun getNotification(){
//        viewModelScope.launch {
//            _notifData.value = ResourcesResult.Loading
//            val result = notificationRepository.fetchDataNotification()
//            _notifData.value = result
//        }
//    }

    val getDataNotification = notificationRepository.fetchDataNotification()

    fun updateNotification(notification: Notification) {
        viewModelScope.launch {
            notificationRepository.updateValues(notification)
        }
    }

}