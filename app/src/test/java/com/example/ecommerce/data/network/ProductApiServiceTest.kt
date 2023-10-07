package com.example.ecommerce.data.network

import com.example.ecommerce.core.data.models.request.FullfilmentRequest
import com.example.ecommerce.core.data.models.request.RatingRequest
import com.example.ecommerce.core.data.models.response.FulfillmentDetail
import com.example.ecommerce.core.data.models.response.FullfilmentResponse
import com.example.ecommerce.core.data.models.response.ItemsItem
import com.example.ecommerce.core.data.models.response.PaymentItem
import com.example.ecommerce.core.data.models.response.PaymentResponse
import com.example.ecommerce.core.data.models.response.PaymentType
import com.example.ecommerce.core.data.models.response.ProductData
import com.example.ecommerce.core.data.models.response.ProductDetailData
import com.example.ecommerce.core.data.models.response.ProductDetailResponse
import com.example.ecommerce.core.data.models.response.ProductResponse
import com.example.ecommerce.core.data.models.response.ProductVariantItem
import com.example.ecommerce.core.data.models.response.RatingResponse
import com.example.ecommerce.core.data.models.response.ReviewDataItem
import com.example.ecommerce.core.data.models.response.ReviewResponse
import com.example.ecommerce.core.data.models.response.SearchResponse
import com.example.ecommerce.core.data.models.response.TransactionDataItem
import com.example.ecommerce.core.data.models.response.TransactionResponse
import com.example.ecommerce.core.data.models.response.TranscationsItem
import com.example.ecommerce.core.data.network.ProductApiService
import com.example.ecommerce.utils.JsonConverter.q
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class ProductApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var productApiService: ProductApiService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        productApiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }

    @After
    fun finish(){
        mockWebServer.shutdown()
    }

    @Test
    fun fullfilmentTest() = runTest{
        mockWebServer.q("fullfilment.json", 200)

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
        assertEquals(actualResponse.body(), expectedResponse)
    }

    @Test
    fun payment() = runTest {
        mockWebServer.q("payment.json",200)

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
        assertEquals(actualResponse.body(), expectedResponse)
    }

    @Test
    fun productDetailTest() = runTest {
        mockWebServer.q("product_detail.json", 200)

        val actualResponse = productApiService.details(productId = "123")

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
        assertEquals(actualResponse.body(), expectedResponse)
    }

    @Test
    fun productReviewTest() = runTest {
        mockWebServer.q("product_review.json", 200)

        val actualResponse = productApiService.review(productId = "123")

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
        assertEquals(actualResponse.body(), expectedResponse)
    }

    @Test
    fun productsTest() = runTest {
        mockWebServer.q("products.json", 200)

        val actualResponse = productApiService.products("asus","asus",10,
            10000000,"sales",1,1)

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
        assertEquals(actualResponse, expectedResponse)
    }

    @Test
    fun ratingTest() = runTest {
        mockWebServer.q("rating.json", 200)

        val actualResponse = productApiService.rating(ratingRequest = RatingRequest(
            review = null,
            rating = null,
            invoiceId = null
        )
        )

        val expectedResponse = RatingResponse(
            code = 200,
            message = "message"
        )
        assertEquals(actualResponse.body(),expectedResponse)
    }

    @Test
    fun searchTest() = runTest {
        mockWebServer.q("search.json", 200)

        val actualResponse = productApiService.search(query = "asus")

        val expectedResponse = SearchResponse(
            code = 200,
            message = "OK",
            data = listOf("Lenovo Legion 3")
        )

        assertEquals(actualResponse.body(),expectedResponse)
    }

    @Test
    fun transactionTest() = runTest {
        mockWebServer.q("transaction.json", 200)

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
        assertEquals(actualResponse.body(),expectedResponse)
    }

}