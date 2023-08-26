package com.example.ecommerce.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.data.models.request.ProductRequest
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.models.response.SearchResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val sharedPreferencesManager : PreferenceProvider) : ViewModel() {

    private val _searchResult = MutableLiveData<ResourcesResult<SearchResponse<List<String>>?>>()
    val searchResult : LiveData<ResourcesResult<SearchResponse<List<String>>?>> = _searchResult

    val product : LiveData<PagingData<ItemsItem>> =
        productRepository.getProduct(ProductRequest()).cachedIn(viewModelScope)

    var inputText: String = ""

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }

    fun searchItem(query : String){
        viewModelScope.launch {
            _searchResult.value = ResourcesResult.Loading
            val result = productRepository.searchProduct(query)
            _searchResult.value = result
        }
    }


}