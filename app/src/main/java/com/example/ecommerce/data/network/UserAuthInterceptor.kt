package com.example.ecommerce.data.network

import com.example.ecommerce.utils.Constants.API_KEY
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UserAuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val modifiedRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("API_KEY", API_KEY)
            .build()

        return chain.proceed(modifiedRequest)

    }
}