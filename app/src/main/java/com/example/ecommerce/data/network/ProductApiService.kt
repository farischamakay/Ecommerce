package com.example.ecommerce.data.network

import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.data.models.response.FullfilmentResponse
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.data.models.response.ProductResponse
import com.example.ecommerce.data.models.response.RatingResponse
import com.example.ecommerce.data.models.response.ReviewResponse
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.models.response.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {
    @POST("products")
    suspend fun products(
        @Query("search") search: String?,
        @Query("brand") brand: String?,
        @Query("lowest") lowest: Int?,
        @Query("highest") highest: Int?,
        @Query("sort") sort: String?,
        @Query("limit") limit: Int?,
        @Query("page") page: Int?
    ): ProductResponse

    @POST("search")
    suspend fun search(
        @Query("query") query: String?
    ): Response<SearchResponse<List<String>>>

    @GET("products/{id}")
    suspend fun details(
        @Path("id") productId: String?
    ): Response<ProductDetailResponse>

    @GET("review/{id}")
    suspend fun review(
        @Path("id") productId: String?
    ): Response<ReviewResponse>

    @GET("payment")
    suspend fun payment(

    ): Response<PaymentResponse>

    @GET("transaction")
    suspend fun transaction(

    ): Response<TransactionResponse>

    @POST("fulfillment")
    suspend fun fullfilment(
        @Body fullfilmentRequest: FullfilmentRequest
    ): Response<FullfilmentResponse>

    @POST("rating")
    suspend fun rating(
        @Body ratingRequest: RatingRequest
    ): Response<RatingResponse>

}