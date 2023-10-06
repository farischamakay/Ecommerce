package com.example.ecommerce.main.transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.core.data.models.response.TransactionResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(private val productRepository: ProductRepository) :
    ViewModel() {

    private val _transactionResult = MutableLiveData<ResourcesResult<TransactionResponse?>>()
    val paymentResult: MutableLiveData<ResourcesResult<TransactionResponse?>> = _transactionResult

    fun fetchTransaction() {
        viewModelScope.launch {
            _transactionResult.value = ResourcesResult.Loading
            val result = productRepository.transactions()
            _transactionResult.value = result
        }
    }
}