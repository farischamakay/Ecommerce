package com.example.ecommerce.data.database.notification

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotificationDao {
    @Query("Select * from notification ORDER BY id DESC")
    fun getAll(): LiveData<List<Notification>>

    @Insert
    fun insertNotification(notification: Notification)

    @Update
    suspend fun updateNotification(notification: Notification)

}
