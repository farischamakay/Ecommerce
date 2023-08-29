package com.example.ecommerce.data.network

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.data.models.request.RefreshRequest
import com.example.ecommerce.data.models.response.RefreshResponse
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.Constants.BASE_URL
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val sharedPreferenceManager: PreferenceProvider,
    private val chuckerInterceptor: ChuckerInterceptor,
    private val userAuthInterceptor: UserAuthInterceptor
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            val token = runBlocking {
                sharedPreferenceManager.getRefreshKey()
            }
            return runBlocking {
                try {
                    val newToken = getNewToken(token)
                    if (!newToken.isSuccessful || newToken.body() == null) {
                        sharedPreferenceManager.deleteTokenAccess()
                    }
                    newToken.body()?.let {
                        sharedPreferenceManager.saveAccess(
                            it.data?.accessToken.toString(),
                            it.data?.refreshToken.toString()
                        )
                        response.request.newBuilder()
                            .header("Authorization", "Bearer ${it.data?.accessToken}")
                            .build()
                    }
                } catch (error: HttpException) {
                    if (error.code() == 401) {
                        println("EXPIRED TOKEN")
                        null
                    } else {
                        null
                    }
                }
            }
        }
    }

    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<RefreshResponse> {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(userAuthInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(UserApiService::class.java)
        val token = RefreshRequest(refreshToken)
        return service.refresh(token)
    }
}