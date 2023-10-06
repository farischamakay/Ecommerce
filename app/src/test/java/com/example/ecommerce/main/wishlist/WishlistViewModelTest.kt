package com.example.ecommerce.main.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.database.cart.Cart
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class WishlistViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var roomCartRepository: RoomCartRepository
    private lateinit var wishlistViewModel: WishlistViewModel

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
    @Before
    fun setUp() {
        roomCartRepository = mock()
    }

    @Test
    fun getGetDataWishlist() {
        val actualResponse = roomCartRepository.fetchWishlistData()
        val expectedResponse = wishlist
        whenever(actualResponse).thenReturn(liveDataOf(listOf(expectedResponse)))
        wishlistViewModel = WishlistViewModel(roomCartRepository)
        assertEquals(listOf(expectedResponse), wishlistViewModel.getDataWishlist.getOrAwaitValue())
    }

    @Test
    fun getGetDataRoom() {
        val actualResponse = roomCartRepository.fetchCartData()
        val expectedResponse = cart
        whenever(actualResponse).thenReturn(liveDataOf(listOf(expectedResponse)))
        wishlistViewModel = WishlistViewModel(roomCartRepository)
        assertEquals(listOf(expectedResponse), wishlistViewModel.getDataRoom.getOrAwaitValue())
    }

    @Test
    fun deleteItemById() = runTest{
        val actualResponse = roomCartRepository.deleteById(cart.productId)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            wishlistViewModel = WishlistViewModel(roomCartRepository)
            assertEquals(Unit, wishlistViewModel.deleteItemById(cart.productId))
        }
    }

    @Test
    fun insertToRoom() = runTest{
        val actualResponse = roomCartRepository.insertCartData(cart)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            wishlistViewModel = WishlistViewModel(roomCartRepository)
            assertEquals(Unit, wishlistViewModel.insertToRoom(cart))
        }
    }

    @Test
    fun updateQuantity() = runTest{
        val actualResponse = roomCartRepository.updateValues(listOf(cart.copy(quantity = 2)))
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            wishlistViewModel = WishlistViewModel(roomCartRepository)
            assertEquals(Unit, wishlistViewModel.updateQuantity(listOf(Pair(cart, 5))))
        }
    }
}