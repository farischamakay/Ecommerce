package com.example.ecommerce.main.cart

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.models.response.PaymentResponse
import com.example.ecommerce.data.repository.ProductRepository
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
class PaymentViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val remoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _paymentResult = MutableLiveData<ResourcesResult<PaymentResponse?>>()
    val paymentResult: LiveData<ResourcesResult<PaymentResponse?>> = _paymentResult

    init {
        fetchConfig()
    }

    private fun fetchPayment() {
        viewModelScope.launch {
            _paymentResult.value = ResourcesResult.Loading
            val result = productRepository.paymentProduct()
            _paymentResult.value = result
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
                    Log.w(ContentValues.TAG, "Config update error with code: ")
                }
            }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.d(ContentValues.TAG, "Updated keys: " + configUpdate.updatedKeys)

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
                Log.w(ContentValues.TAG, "Config update error with code: " + error.code, error)
            }
        })
    }
}