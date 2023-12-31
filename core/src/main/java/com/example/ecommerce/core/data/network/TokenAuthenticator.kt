package com.example.ecommerce.core.data.network

import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.core.data.models.request.RefreshRequest
import com.example.ecommerce.core.data.models.response.RefreshResponse
import com.example.ecommerce.core.data.preferences.PreferenceProvider
import com.example.ecommerce.core.data.utils.Constants.BASE_URL
import com.example.ecommerce.core.data.utils.SessionManager
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
    private val userAuthInterceptor: UserAuthInterceptor,
    private val sessionManager: SessionManager
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
                        if (newToken.code() == 401) {
                            Log.d("exired_token", "EXPIRED TOKEN")
                            sessionManager.setTokenExpired()
                            sharedPreferenceManager.deleteTokenAccess()
                            null
                        } else {
                            null
                        }
                    } else
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
                    null
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