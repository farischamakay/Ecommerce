package com.example.ecommerce.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.data.models.response.FullfilmentResponse
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.models.response.ProductDetailResponse
import com.example.ecommerce.data.models.response.RatingResponse
import com.example.ecommerce.data.models.response.ReviewResponse
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.network.ProductApiService
import com.example.ecommerce.utils.ResourcesResult
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService
) {
    fun getProduct(
        search: String?,
        brand: String?,
        lowest: Int?,
        highest: Int?,
        sort: String?
    ): LiveData<PagingData<ItemsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ProductPagingSource(productApiService, search, brand, lowest, highest, sort)
            }
        ).liveData
    }

    suspend fun searchProduct(query: String): ResourcesResult<SearchResponse<List<String>>?> {
        return try {
            val response = productApiService.search(query)
            if (response.isSuccessful) {
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception: Exception) {
            ResourcesResult.Failure(exception.message)
        }
    }

    suspend fun detailProduct(productId: String): ResourcesResult<ProductDetailResponse?> {
        return try {
            val response = productApiService.details(productId)
            if (response.isSuccessful) {
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception: Exception) {
            ResourcesResult.Failure(exception.message)
        }
    }

    suspend fun reviewProduct(productId: String): ResourcesResult<ReviewResponse?> {
        return try {
            val response = productApiService.review(productId)
            if (response.isSuccessful) {
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception: Exception) {
            ResourcesResult.Failure(exception.message)
        }
    }

    suspend fun paymentProduct() : ResourcesResult<PaymentResponse?> {
        return try {
            val response = productApiService.payment()
            if (response.isSuccessful) {
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception: Exception) {
            ResourcesResult.Failure(exception.message)
        }
    }
    suspend fun fullfilmentPayment(fullfilmentRequest: FullfilmentRequest) : ResourcesResult<FullfilmentResponse?>{
        return  try {
            val response = productApiService.fullfilment(fullfilmentRequest)
            if(response.isSuccessful){
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception : Exception){
            ResourcesResult.Failure(exception.message)
        }
    }
    suspend fun ratingProduct(ratingRequest: RatingRequest) : ResourcesResult<RatingResponse?>{
        return try {
            val response = productApiService.rating(ratingRequest)
            if (response.isSuccessful){
                val data = response.body()
                ResourcesResult.Success(data)
            } else {
                ResourcesResult.Failure(response.message())
            }
        } catch (exception : Exception){
            ResourcesResult.Failure(exception.message)
        }
    }

}