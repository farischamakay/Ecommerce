package com.example.ecommerce.main.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.database.wishlist.Wishlist
import com.example.ecommerce.data.models.response.ProductDetailData
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.data.models.response.ProductVariantItem
import com.example.ecommerce.data.models.response.ReviewDataItem
import com.example.ecommerce.data.models.response.ReviewResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.utils.ResourcesResult
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
class StoreViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var productRepository: ProductRepository
    private lateinit var roomCartRepository: RoomCartRepository
    private lateinit var storeViewModel: StoreViewModel

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
        productRepository = mock()
        roomCartRepository = mock()
    }

    @Test
    fun getDetailProduct() = runTest {
        val actualResponse = productRepository.detailProduct(productId = "123")
        val expectedResponse = ProductDetailResponse(
            code = 200,
            message = "OK",
            data = ProductDetailData(
                productId = "productId",
                productName = "productName",
                productPrice = 24499000,
                image = listOf("https:image1","https:image2"),
                brand = "brand",
                description = "description",
                store = "store",
                sale = 12,
                stock = 2,
                totalRating = 7,
                totalReview = 5,
                totalSatisfaction = 100,
                productRating = 5.0,
                productVariant = listOf(
                    ProductVariantItem(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                )
                )
            )
        )
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(ResourcesResult.Success(expectedResponse), storeViewModel.detailProduct.getOrAwaitValue())
        }
    }

    @Test
    fun getParam() {
    }

    @Test
    fun getDetail() {
    }

    @Test
    fun getGetDataRoom() {
        val actualResponse = roomCartRepository.fetchCartData()
        val expectedResponse = cart
        whenever(actualResponse).thenReturn(liveDataOf(listOf(cart)))
        storeViewModel = StoreViewModel(productRepository, roomCartRepository)
        assertEquals(listOf(expectedResponse), storeViewModel.getDataRoom.getOrAwaitValue())
    }

    @Test
    fun getGetDataWishlist() {
        val actualResponse = roomCartRepository.fetchWishlistData()
        val expectedResponse = wishlist
        whenever(actualResponse).thenReturn(liveDataOf(listOf(wishlist)))
        storeViewModel = StoreViewModel(productRepository, roomCartRepository)
        assertEquals(listOf(expectedResponse), storeViewModel.getDataWishlist.getOrAwaitValue())

    }

    @Test
    fun getProduct() {
    }

    @Test
    fun setQuery() {
    }

    @Test
    fun searchItem() {
    }

    @Test
    fun detailItem() {
    }

    @Test
    fun reviewItem() = runTest{
        val actualResponse = productRepository.reviewProduct(productId = "123")
        val expectedResponse = ReviewResponse(
            code = 200,
            message = "OK",
            data = listOf(
                ReviewDataItem(
                    userName= "userName",
                    userImage = "userImage",
                    userRating = 4,
                    userReview= "userReview"
                )
            )
        )
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(ResourcesResult.Success(expectedResponse))
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(Unit, storeViewModel.reviewItem("123"))
        }
    }

    @Test
    fun insertToRoom() = runTest{
        val actualResponse = roomCartRepository.insertCartData(cart)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(Unit, storeViewModel.insertToRoom(cart))
        }
    }

    @Test
    fun insertToWishlist() = runTest {
        val actualResponse = roomCartRepository.insertWishlistData(wishlist)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(Unit, storeViewModel.insertToWishlist(wishlist))
        }
    }

    @Test
    fun updateQuantity() = runTest{
        val actualResponse = roomCartRepository.updateValues(listOf(cart.copy(quantity = 2)))
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(Unit, storeViewModel.updateQuantity(listOf(Pair(cart, 5))))
        }
    }

    @Test
    fun deleteItemById() = runTest{
        val actualResponse = roomCartRepository.deleteById(cart.productId)
        backgroundScope.launch {
            whenever(actualResponse).thenReturn(Unit)
            storeViewModel = StoreViewModel(productRepository, roomCartRepository)
            assertEquals(Unit, storeViewModel.deleteItemById(cart.productId))
        }
    }
}