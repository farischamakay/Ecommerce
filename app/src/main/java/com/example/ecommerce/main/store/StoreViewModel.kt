package com.example.ecommerce.main.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.data.models.request.ProductRequest
import com.example.ecommerce.data.models.response.ItemsItem
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.preferences.PreferenceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val sharedPreferencesManager : PreferenceProvider) : ViewModel() {

    fun product (productRequest: ProductRequest) : LiveData<PagingData<ItemsItem>> =
        productRepository.getProduct(productRequest).cachedIn(viewModelScope)

    fun getUsername(): String? {
        return sharedPreferencesManager.getUsername()
    }
}