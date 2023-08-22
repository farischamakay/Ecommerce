package com.example.ecommerce.data.network


import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UserAuthInterceptor @Inject constructor(
    private val sharedPreferenceManager: PreferenceProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val modifiedRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("API_KEY", API_KEY)
            .addHeader("Authorization", "Bearer ${sharedPreferenceManager.getApiAccessKey()}")
            .build()

        return chain.proceed(modifiedRequest)

    }
}