package com.example.ecommerce.di

import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.ecommerce.data.network.TokenAuthenticator
import com.example.ecommerce.data.network.UserApiService
import com.example.ecommerce.data.network.UserAuthInterceptor
import com.example.ecommerce.preferences.PreferenceProvider
import com.example.ecommerce.utils.Constants.BASE_URL
import com.example.ecommerce.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("shared_pref", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideTokenAuthenticator(
        sharedPreferences: PreferenceProvider,
        chuckerInterceptor: ChuckerInterceptor,
        userAuthInterceptor: UserAuthInterceptor,
        sessionManager: SessionManager
    ): TokenAuthenticator {
        return TokenAuthenticator(
            sharedPreferences,
            chuckerInterceptor,
            userAuthInterceptor,
            sessionManager
        )
    }

    @Provides
    @Singleton
    fun provideChucker(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: UserAuthInterceptor,
        chuckerInterceptor: ChuckerInterceptor,
        tokenInterceptor: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(chuckerInterceptor)
            .authenticator(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }


}