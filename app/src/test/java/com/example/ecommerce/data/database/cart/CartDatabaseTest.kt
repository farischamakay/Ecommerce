package com.example.ecommerce.data.database.cart

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.data.database.notification.NotificationDao
import com.example.ecommerce.data.database.wishlist.WishlistDao
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDatabaseTest {

    private lateinit var cartDao: CartDao
    private lateinit var wishlistDao: WishlistDao
    private lateinit var notificationDao : NotificationDao
    private lateinit var db : CartDatabase


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CartDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        cartDao = db.cartDao()
        wishlistDao = db.wishlistDao()
        notificationDao = db.notificationDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

//    @Test
//    fun testCartDao() = runTest {
//        val cart = Cart(null,null,null,null,null,null,null,
//            null,null,null,null,0,0,false,1)
//    }

    @Test
    fun wishlistDao() {

    }

    @Test
    fun notificationDao() {

    }
}