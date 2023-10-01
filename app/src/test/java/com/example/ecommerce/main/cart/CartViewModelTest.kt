package com.example.ecommerce.main.cart

import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

@RunWith(JUnit4::class)
class CartViewModelTest {

    private lateinit var roomCartRepository: RoomCartRepository
    private lateinit var productRepository: ProductRepository
    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var cartViewModel : CartViewModel

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

    @Before
    fun setUp() {
        roomCartRepository = mock()
        productRepository = mock()
        remoteConfig = mock()
        cartViewModel = CartViewModel(roomCartRepository,productRepository,remoteConfig)
    }

    @Test
    fun getPaymentResult() {

    }

    @Test
    fun getFulfillmentResult() {
    }

    @Test
    fun getRatingResult() {
    }

    @Test
    fun getGetDataRoom() {
    }

    @Test
    fun updateCheckable() = runTest {
//        val actualResult = roomCartRepository.updateValues(listOf(cart.copy(isCheck = true)))
//        val expectedResult = listOf(Pair(cart, cart.isCheck))
//        whenever(actualResult).thenReturn(Unit)
//        assertEquals(Unit, cartViewModel.updateCheckable(expectedResult))
    }

    @Test
    fun updateQuantity() {
    }

    @Test
    fun deleteItemById() {
    }

    @Test
    fun deleteCheckedItems() {
    }

    @Test
    fun selectedAllItems() {
    }

    @Test
    fun fulfillment() {
    }

    @Test
    fun rating() {
    }
}