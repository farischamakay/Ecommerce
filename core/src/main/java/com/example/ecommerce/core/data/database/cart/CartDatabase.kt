package com.example.ecommerce.core.data.database.cart

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.core.data.database.notification.NotificationDao
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.core.data.database.wishlist.WishlistDao

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