package com.example.ecommerce.core.data.network


import com.example.ecommerce.core.data.preferences.PreferenceProvider
import com.example.ecommerce.core.data.utils.Constants.API_KEY
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UserAuthInterceptor @Inject constructor(
    private val sharedPreferenceManager: PreferenceProvider
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = runBlocking {
            sharedPreferenceManager.getApiAccessKey()
        }
        print("test")

        val modifiedRequest = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("API_KEY", API_KEY)
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)

    }
}