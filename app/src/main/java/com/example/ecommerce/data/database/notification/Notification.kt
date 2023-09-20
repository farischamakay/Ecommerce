package com.example.ecommerce.data.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notification")
data class Notification(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val body : String,
    val image : String,
    val type : String,
    val date : String,
    val time : String,
    var isSelected : Boolean = false
)