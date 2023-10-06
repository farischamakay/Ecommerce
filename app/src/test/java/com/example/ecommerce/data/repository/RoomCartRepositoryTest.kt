package com.example.ecommerce.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.core.data.database.cart.Cart
import com.example.ecommerce.core.data.database.cart.CartDao
import com.example.ecommerce.core.data.database.wishlist.Wishlist
import com.example.ecommerce.core.data.database.wishlist.WishlistDao
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RoomCartRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var roomCartRepository: RoomCartRepository
    private lateinit var cartDao: CartDao
    private lateinit var wishlistDao: WishlistDao

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
        cartDao = mock()
        wishlistDao = mock()
        roomCartRepository = RoomCartRepository(cartDao, wishlistDao)
    }

    @Test
    fun fetchCartData() {
        val actualResponse = cartDao.getAll()
        val expectedResponse = cart
        whenever(actualResponse).thenReturn(liveDataOf(listOf(expectedResponse)))
        val result = roomCartRepository.fetchCartData().getOrAwaitValue()
        assertEquals(result, listOf(expectedResponse))
    }

    @Test
    fun insertCartData() = runTest{
        val actualResponse = cartDao.insertCart(cart)
        val expectedResponse = cart
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.insertCartData(expectedResponse)
        assertEquals(Unit, result)
    }

    @Test
    fun updateValues() = runTest{
        val actualResponse = cartDao.update(cart.copy(totalReview = 10))
        val expectedResponse = cart.copy(totalReview = 10)
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.updateValues(listOf(expectedResponse))
        assertEquals(Unit, result)
    }

    @Test
    fun deleteById() = runTest{
        val actualResponse = cartDao.deleteById(itemId = "123")
        val expectedResponse = cart.productId
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.deleteById(expectedResponse)
        assertEquals(Unit, result)
    }

    @Test
    fun deleteData() = runTest{
        val actualResponse = cartDao.delete(cart)
        val expectedResponse = cart
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.deleteData(expectedResponse)
        assertEquals(Unit, result)
    }

    @Test
    fun fetchWishlistData() {
        val actualResponse = wishlistDao.getAll()
        val expectedResponse = wishlist
        whenever(actualResponse).thenReturn(liveDataOf(listOf(expectedResponse)))
        val result = roomCartRepository.fetchWishlistData().getOrAwaitValue()
        assertEquals(listOf(expectedResponse),result)
    }

    @Test
    fun insertWishlistData() = runTest{
        val actualResponse = wishlistDao.insertWishlist(wishlist)
        val expectedResponse = wishlist
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.insertWishlistData(expectedResponse)
        assertEquals(Unit, result)
    }
    @Test
    fun deleteWishlistById() = runTest{
        val actualResponse = wishlistDao.deleteWishlistById(itemId = "123")
        val expectedResponse = wishlist.productId
        whenever(actualResponse).thenReturn(Unit)
        val result = roomCartRepository.deleteWishlistById(expectedResponse)
        assertEquals(Unit, result)
    }
}