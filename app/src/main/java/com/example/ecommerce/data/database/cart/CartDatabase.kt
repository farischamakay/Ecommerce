package com.example.ecommerce.data.database.cart

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.data.database.notification.Notification
import com.example.ecommerce.data.database.notification.NotificationDao
import com.example.ecommerce.data.database.wishlist.Wishlist
import com.example.ecommerce.data.database.wishlist.WishlistDao

@Database(
    entities = [Cart::class, Wishlist::class, Notification::class],
    version = 1,
    exportSchema = false
)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun notificationDao(): NotificationDao

}