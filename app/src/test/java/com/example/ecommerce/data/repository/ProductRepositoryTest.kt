package com.example.ecommerce.data.repository

import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.data.models.response.FulfillmentDetail
import com.example.ecommerce.data.models.response.FullfilmentResponse
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.models.response.PaymentItem
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.models.response.PaymentType
import com.example.ecommerce.data.models.response.ProductData
import com.example.ecommerce.data.models.response.ProductDetailData
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.data.models.response.ProductResponse
import com.example.ecommerce.data.models.response.ProductVariantItem
import com.example.ecommerce.data.models.response.RatingResponse
import com.example.ecommerce.data.models.response.ReviewDataItem
import com.example.ecommerce.data.models.response.ReviewResponse
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.models.response.TransactionDataItem
import com.example.ecommerce.data.models.response.TransactionResponse
import com.example.ecommerce.data.models.response.TranscationsItem
import com.example.ecommerce.core.data.network.ProductApiService
import com.example.ecommerce.utils.ResourcesResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

class ProductRepositoryTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var productApiService: ProductApiService

    @Before
    fun setUp() {
        productApiService = mock()
        productRepository = ProductRepository(productApiService)
    }

    @Test
    fun getProduct() = runTest {
        val actualResponse = productApiService.products(search = null, brand = null, lowest = null,
            highest = null, limit = null, sort = null, page = null)
        val expectedResponse = ProductResponse(
            code = 200,
            message = "OK",
            data = ProductData(
                itemsPerPage = 10,
                currentItemCount = 10,
                pageIndex = 1,
                totalPages = 3,
                items = listOf(
                    ItemsItem(
                    productId = "productId",
                    productName = "productName",
                    productPrice = 15000000,
                    image = "image",
                    brand = "brand",
                    store = "store",
                    sale = 4,
                    productRating = 4.0
                )
                )
            )
        )
        whenever(actualResponse).thenReturn(expectedResponse)
        val result = productRepository.getProduct(search = null, brand = null, lowest = null,
            highest = null, sort = null)
//        assertEquals(expectedResponse, result)
    }

    @Test
    fun searchProduct() = runTest{
        val actualResponse = productApiService.search(query = "asus")

        val expectedResponse = SearchResponse(
            code = 200,
            message = "OK",
            data = listOf("Lenovo Legion 3")
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.searchProduct(query = "asus")
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun detailProduct() = runTest{
        val actualResponse = productApiService.details(productId = "1234")

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
                productVariant = listOf(ProductVariantItem(
                    variantName = "RAM 16GB",
                    variantPrice = 0
                ))
            )
        )

        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.detailProduct(productId = "1234")
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun reviewProduct() = runTest {
        val actualResponse = productApiService.review(productId = "1234")
        val expectedResponse = ReviewResponse(
            code = 200,
            message = "OK",
            data = listOf(
                ReviewDataItem(
                userName= "userName",
                userImage = "userImage",
                userRating = 4,
                userReview= "userReview"
            ))
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.reviewProduct(productId = "1234")
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun paymentProduct() = runTest {
        val actualResponse = productApiService.payment()
        val expectedResponse = PaymentResponse(
            code = 200,
            message = "OK",
            data = listOf(
                PaymentType(
                    title = "Transfer Virtual Account",
                    item = listOf(
                        PaymentItem(
                        label = "BCA Virtual Account",
                        image = "image",
                        status = true
                    )
                    )
                )
            )
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.paymentProduct()
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun fullfilmentPayment() = runTest {
        val actualResponse = productApiService
            .fullfilment(fullfilmentRequest = FullfilmentRequest(null,null))
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
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.fullfilmentPayment(FullfilmentRequest(null, null))
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun ratingProduct() = runTest{
        val actualResponse = productApiService.rating(ratingRequest = RatingRequest(review = null,
            rating = null,
            invoiceId = null))
        val expectedResponse = RatingResponse(
            code = 200,
            message = "message"
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.ratingProduct(ratingRequest = RatingRequest(review = null,
            rating = null,
            invoiceId = null))
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }

    @Test
    fun transactions() = runTest{
        val actualResponse = productApiService.transaction()
        val expectedResponse = TransactionResponse(
            code = 200,
            message = "OK",
            data = listOf(
                TransactionDataItem(
                invoiceId = "invoiceId",
                status = true,
                date = "date",
                time = "time",
                payment = "payment",
                total = 1000000,
                rating = 4,
                review = "review",
                image = "image",
                name = "name",
                items = listOf(
                    TranscationsItem(
                    productId = "productId",
                    variantName = "variantName",
                    quantity = 2
                )
                )
            )
            )
        )
        whenever(actualResponse).thenReturn(Response.success(expectedResponse))
        val result = productRepository.transactions()
        assertEquals((result as ResourcesResult.Success).data, expectedResponse)
    }
}