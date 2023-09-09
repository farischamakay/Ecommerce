package com.example.ecommerce.main.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.models.response.ProfileResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.utils.ResourcesResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val roomCartRepository: RoomCartRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _paymentResult = MutableLiveData<ResourcesResult<PaymentResponse?>>()
    val paymentResult: MutableLiveData<ResourcesResult<PaymentResponse?>> = _paymentResult

    val getDataRoom =
        roomCartRepository.fetchCartData()

    fun updateCheckable(cartList: List<Pair<Cart, Boolean>>) {
        viewModelScope.launch {
            val updates = cartList.map { (cartList, isChecked) ->
                cartList.copy(isCheck = isChecked)
            }
            roomCartRepository.updateValues(updates)
        }
    }

    fun updateQuantity(cartList: List<Pair<Cart, Int>>) {
        viewModelScope.launch {
            val updates = cartList.map { (cartList, quantity) ->
                cartList.copy(quantity = quantity)
            }
            roomCartRepository.updateValues(updates)
        }
    }

    fun deleteItemById(itemId : String){
        viewModelScope.launch {
            roomCartRepository.deleteById(itemId)
        }
    }
    fun deleteCheckedItems() {
        viewModelScope.launch {
            val checkedItems = getDataRoom.value?.filter { it.isCheck } ?: emptyList()
            roomCartRepository.deleteData(*checkedItems.toTypedArray())
        }
    }
    fun selectedAllItems(isChecked: Boolean) {
        viewModelScope.launch {
            val cartList = getDataRoom.value ?: emptyList()
            val updateList = cartList.map { it.copy(isCheck = isChecked) }
            roomCartRepository.updateValues(updateList)
        }
    }

    fun fetchPayment() {
        viewModelScope.launch {
            _paymentResult.value = ResourcesResult.Loading
            val result = productRepository.paymentProduct()
            _paymentResult.value = result
        }
    }
}