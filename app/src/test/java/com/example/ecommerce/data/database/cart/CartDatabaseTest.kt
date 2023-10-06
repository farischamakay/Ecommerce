package com.example.ecommerce.data.database.cart

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.ecommerce.core.data.database.cart.Cart
import com.example.ecommerce.core.data.database.cart.CartDao
import com.example.ecommerce.core.data.database.cart.CartDatabase
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.core.data.database.notification.NotificationDao
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.core.data.database.wishlist.WishlistDao
import com.example.ecommerce.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CartDatabaseTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
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

    private val cart = Cart("1",
        "Laptop Asus VivoBook",
        "https:/image",
        "Asus",
        "Laptop Asus keluaran 2022, 16GB, Intel core i5",
        "Official Asus",
        "19",
        5,
        9,
        19,
        4,
        "16 GB",
        0,
        false,
        1)

    private val wishlist = Wishlist("1",
        "Laptop Asus VivoBook",
        "https:/image",
        "Asus",
        "Laptop Asus keluaran 2022, 16GB, Intel core i5",
        "Official Asus",
        "19",
        5,
        9,
        19,
        "4.5",
        5,
        "16 GB",
        5000000,
        false
    )

    private val notification = Notification(1,
        "Promo Telkomsel",
        "Promo tahunan hari ini sebesar 1.000 per item",
        "https:image",
        "Promo",
        "24/03/23",
        "14.30")

    @Test
    fun testCartDao() = runTest {
        cartDao.insertCart(cart)
        val result = cartDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(cart))
    }

    @Test
    fun updateCartDao() = runTest {
        cartDao.insertCart(cart)
        val updatedCart = cart.copy(quantity = 2)
        cartDao.update(updatedCart)
        val result = cartDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(updatedCart))
    }

    @Test
    fun deleteCartById () = runTest {
        cartDao.insertCart(cart)
        cartDao.deleteById("1")
        val result = cartDao.getAll().getOrAwaitValue()
        assertEquals(result, emptyList<Cart>() )
    }

    @Test
    fun deleteCartDao() = runTest {
        cartDao.insertCart(cart)
        cartDao.delete(cart)
        val result = cartDao.getAll().getOrAwaitValue()
        assertEquals(result, emptyList<Cart>())
    }

    @Test
    fun wishlistDao() = runTest {
        wishlistDao.insertWishlist(wishlist)
        val result = wishlistDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(wishlist))
    }

    @Test
    fun wishlistUpdate() = runTest {
        wishlistDao.insertWishlist(wishlist)
        val updateWishlist = wishlist.copy(quantity = 3)
        wishlistDao.updateWishlist(updateWishlist)
        val result = wishlistDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(updateWishlist))
    }

    @Test
    fun deleteWishlist() = runTest {
        wishlistDao.insertWishlist(wishlist)
        wishlistDao.deleteWishlistById(wishlist.productId)
        val result = wishlistDao.getAll().getOrAwaitValue()
        assertEquals(result, emptyList<Wishlist>())
    }

    @Test
    fun notificationDao() = runTest {
        notificationDao.insertNotification(notification)
        val result = notificationDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(notification))
    }

    @Test
    fun updateNotification() = runTest {
        notificationDao.insertNotification(notification)
        val updateNotif = notification.copy(isSelected = true)
        notificationDao.updateNotification(updateNotif)
        val result = notificationDao.getAll().getOrAwaitValue()
        assertEquals(result, listOf(updateNotif))

    }
}