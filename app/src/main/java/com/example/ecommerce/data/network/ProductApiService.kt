package com.example.ecommerce.data.network

import com.example.ecommerce.data.models.response.ProductResponse
import com.example.ecommerce.data.models.response.SearchResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApiService {
    @POST("products")
    suspend fun products(
        @Query("search") search : String?,
        @Query("brand") brand : String?,
        @Query("lowest") lowest : Int?,
        @Query("highest") highest : Int?,
        @Query("sort") sort : String?,
        @Query("limit") limit : Int?,
        @Query("page") page : Int?
    ) : ProductResponse

    @POST("search")
    suspend fun search(
        @Query("query") query  : String?
    ) : Response<SearchResponse<List<String>>>

}