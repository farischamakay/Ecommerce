package com.example.ecommerce.main.notification


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.data.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    val getDataNotification = notificationRepository.fetchDataNotification()

    fun updateNotification(notification: Notification) {
        viewModelScope.launch {
            notificationRepository.updateValues(notification)

        }
    }

}