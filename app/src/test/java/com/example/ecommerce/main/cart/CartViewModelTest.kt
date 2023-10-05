package com.example.ecommerce.main.cart

import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.data.models.response.FulfillmentDetail
import com.example.ecommerce.data.models.response.FullfilmentResponse
import com.example.ecommerce.data.models.response.RatingResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.utils.ResourcesResult
import com.example.ecommerce.utils.getOrAwaitValue
import com.example.ecommerce.utils.liveDataOf
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
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
        cartViewModel = CartViewModel(roomCartRepository,productRepository)
    }

    @Test
    fun getFulfillmentResult()  = runTest {
        val actualResponse = productRepository.fullfilmentPayment(fullfilmentRequest = FullfilmentRequest(null,null
        ))
        val expectedResponse = FullfilmentResponse(
            code = 200,
            message = "OK",
            data = FulfillmentDetail(
                invoiceId = "invoiceId",
                status= true,
                date = "date",
                time = "time",
                payment = "payment",
                total = 48998000
            )
        )
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
            assertEquals(ResourcesResult.Success(expectedResponse), cartViewModel.fulfillmentResult.getOrAwaitValue())
        }
    }

    @Test
    fun getRatingResult() = runTest {
        val actualResponse = productRepository.ratingProduct(ratingRequest = RatingRequest(review = null,
            rating = null,
            invoiceId = null))
        val expectedResponse = RatingResponse(
            code = 200,
            message = "message"
        )
        backgroundScope.launch{
            whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
            assertEquals(ResourcesResult.Success(expectedResponse), cartViewModel.ratingResult.getOrAwaitValue())
        }

    }

    @Test
    fun getGetDataRoom() = runTest{
        val actualResponse = roomCartRepository.fetchCartData()
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(liveDataOf(listOf(cart)))
            assertEquals(listOf(cart), cartViewModel.getDataRoom.getOrAwaitValue())
        }
    }

    @Test
    fun updateCheckable() = runTest {
        val actualResult = roomCartRepository.updateValues(listOf(cart.copy(isCheck = true)))
        val expectedResult = listOf(Pair(cart, cart.isCheck))
        backgroundScope.launch{
            whenever(actualResult).thenReturn(Unit)
            assertEquals(Unit, cartViewModel.updateCheckable(expectedResult))
        }
    }

    @Test
    fun updateQuantity() = runTest {
        val actualResponse = roomCartRepository.updateValues(listOf(cart.copy(quantity = 4)))
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            assertEquals(Unit, cartViewModel.updateQuantity(listOf(Pair(cart, 7))))
        }
    }

    @Test
    fun deleteItemById() = runTest {
        val actualResponse = roomCartRepository.deleteById(cart.productId)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            assertEquals(Unit, cartViewModel.deleteItemById(cart.productId))
        }
    }

    @Test
    fun deleteCheckedItems() = runTest{
        val actualResponse = roomCartRepository.deleteData(cart)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            assertEquals(Unit, cartViewModel.deleteCheckedItems())
        }

    }

    @Test
    fun selectedAllItems() = runTest{
        val actualResponse = roomCartRepository.updateValues(listOf(cart.copy(productId = "123")))
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            assertEquals(Unit, cartViewModel.selectedAllItems(true))
        }


    }

}