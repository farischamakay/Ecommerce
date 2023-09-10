package com.example.ecommerce.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.network.ProductApiService
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val productApiService: ProductApiService,
    private val search: String?,
    private val brand: String?,
    private val lowest: Int?,
    private val highest: Int?,
    private val sort: String?
) :
    PagingSource<Int, ItemsItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ItemsItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemsItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = productApiService.products(
                search,
                brand,
                lowest,
                highest,
                sort,
                null,
                position
            )

            LoadResult.Page(
                data = responseData.data.items,
                prevKey = null,
                nextKey = if (responseData.data.totalPages == position) null else position + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}