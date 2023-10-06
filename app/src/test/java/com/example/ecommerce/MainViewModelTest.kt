package com.example.ecommerce

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.database.cart.Cart
import com.example.ecommerce.core.data.database.notification.Notification
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.data.repository.NotificationRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.core.data.preferences.PreferenceProvider
import com.example.ecommerce.utils.SessionManager
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var roomCartRepository: RoomCartRepository
    private lateinit var sharedPreference: PreferenceProvider
    private lateinit var sessionManager : SessionManager
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var mainViewModel: MainViewModel

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

    @Before
    fun setUp() {
        roomCartRepository = mock()
        sharedPreference = mock()
        sessionManager = mock()
        notificationRepository = mock()
    }

    @Test
    fun getGetDataWishlist() {
        val actualResponse = roomCartRepository.fetchWishlistData()
        whenever(actualResponse).thenReturn(liveDataOf(listOf(wishlist)))
        mainViewModel = MainViewModel(roomCartRepository, notificationRepository, sharedPreference, sessionManager)
        assertEquals(listOf(wishlist), mainViewModel.getDataWishlist.getOrAwaitValue())
    }

    @Test
    fun getGetDataRoom() {
        val actualResponse = roomCartRepository.fetchCartData()
        whenever(actualResponse).thenReturn(liveDataOf(listOf(cart)))
        mainViewModel = MainViewModel(roomCartRepository, notificationRepository, sharedPreference, sessionManager)
        assertEquals(listOf(cart), mainViewModel.getDataRoom.getOrAwaitValue())
    }

    @Test
    fun deleteToken() {
        doNothing(). whenever(sharedPreference).deleteTokenAccess()
        mainViewModel = MainViewModel(roomCartRepository, notificationRepository, sharedPreference, sessionManager)
        assertEquals(Unit, mainViewModel.deleteToken())
    }

    @Test
    fun getGetDataNotification() {
        val actualResponse = notificationRepository.fetchDataNotification()
        whenever(actualResponse).thenReturn(liveDataOf(listOf(notification)))
        mainViewModel = MainViewModel(roomCartRepository, notificationRepository, sharedPreference, sessionManager)
        assertEquals(listOf(notification), mainViewModel.getDataNotification.getOrAwaitValue())
    }

    @Test
    fun getUsername() {
        val actualResponse = sharedPreference.getUsername()
        whenever(actualResponse).thenReturn("123")
        mainViewModel = MainViewModel(roomCartRepository, notificationRepository, sharedPreference, sessionManager)
        assertEquals("123", mainViewModel.getUsername())
    }
}