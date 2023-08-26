package com.example.ecommerce.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.ecommerce.data.models.request.ProductRequest
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.network.ProductApiService
import com.example.ecommerce.utils.ResourcesResult
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productApiService: ProductApiService) {

    fun getProduct(productRequest: ProductRequest) : LiveData<PagingData<ItemsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            pagingSourceFactory = {
                ProductPagingSource(productApiService, productRequest)
            }
        ).liveData
    }

    suspend fun searchProduct(query : String) : ResourcesResult<SearchResponse<List<String>>?>{
        return try {
            val response = productApiService.search(query)
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