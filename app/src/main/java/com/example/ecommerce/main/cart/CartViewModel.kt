package com.example.ecommerce.main.cart

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.database.cart.Cart
import com.example.ecommerce.data.models.request.FullfilmentRequest
import com.example.ecommerce.data.models.request.RatingRequest
import com.example.ecommerce.data.models.response.FullfilmentResponse
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.models.response.RatingResponse
import com.example.ecommerce.data.repository.ProductRepository
import com.example.ecommerce.data.repository.RoomCartRepository
import com.example.ecommerce.utils.ResourcesResult
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val roomCartRepository: RoomCartRepository,
    private val productRepository: ProductRepository,
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _paymentResult = MutableLiveData<ResourcesResult<PaymentResponse?>>()
    private val _fulfillmentResult = MutableLiveData<ResourcesResult<FullfilmentResponse?>>()
    private val _rating = MutableLiveData<ResourcesResult<RatingResponse?>>()

    val paymentResult: LiveData<ResourcesResult<PaymentResponse?>> = _paymentResult
    val fulfillmentResult: LiveData<ResourcesResult<FullfilmentResponse?>> =
        _fulfillmentResult
    val ratingResult: LiveData<ResourcesResult<RatingResponse?>> = _rating

    val getDataRoom =
        roomCartRepository.fetchCartData()


    init {
        fetchConfig()
    }

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

    fun deleteItemById(itemId: String) {
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

    private fun fetchConfig() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = remoteConfig.getString("payments")
                    val gson = Gson()
                    val resultConfig = gson.fromJson(response, PaymentResponse::class.java)
                    _paymentResult.value = ResourcesResult.Success(resultConfig)
                    Log.d("Data remote config", "Data remoteconfig: $response")

                } else {
                    Log.w(TAG, "Config update error with code: ")
                }
            }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(TAG, "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("payments")) {
                    remoteConfig.activate().addOnCompleteListener {
                        val response = remoteConfig.getString("payments")
                        val gson = Gson()
                        val resultConfig = gson.fromJson(response, PaymentResponse::class.java)
                        _paymentResult.value = ResourcesResult.Success(resultConfig)
                        Log.d("Data remote config", "Data remoteconfig: $response")
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w(TAG, "Config update error with code: " + error.code, error)
            }
        })
    }

    private fun fetchPayment() {
        viewModelScope.launch {
            _paymentResult.value = ResourcesResult.Loading
            val result = productRepository.paymentProduct()
            _paymentResult.value = result
        }
    }

    fun fulfillment(fullfilmentRequest: FullfilmentRequest) {
        viewModelScope.launch {
            _fulfillmentResult.value = ResourcesResult.Loading
            val result = productRepository.fullfilmentPayment(fullfilmentRequest)
            _fulfillmentResult.value = result
        }
    }

    fun rating(ratingRequest: RatingRequest) {
        viewModelScope.launch {
            _rating.value = ResourcesResult.Loading
            val result = productRepository.ratingProduct(ratingRequest)
            _rating.value = result
        }
    }

}