package com.example.ecommerce.data.repository

import com.example.ecommerce.data.database.notification.Notification
import com.example.ecommerce.data.database.notification.NotificationDao
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val notificationDao: NotificationDao
) {

    fun fetchDataNotification() = notificationDao.getAll()

    fun insertDataNotification(notification: Notification) =
        notificationDao.insertNotification(notification)

    suspend fun updateValues(notification: Notification) {
        notificationDao.updateNotification(notification)
    }
}