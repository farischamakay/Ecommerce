package com.example.ecommerce.di

import com.example.ecommerce.data.network.UserApiService
import com.example.ecommerce.data.network.UserAuthInterceptor
import com.example.ecommerce.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: UserAuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
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

//    @Singleton
//    @Provides
//    fun providesRetrofitBuilder(): Retrofit.Builder{
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//    }
//
//    @Singleton
//    @Provides
//    fun provideOkHttpClient(authInterceptor: UserAuthInterceptor) : OkHttpClient {
//        return  OkHttpClient.Builder().addInterceptor(authInterceptor).build()
//    }
//
//    @Singleton
//    @Provides
//    fun providesUserApi(retrofitBuilder : Retrofit.Builder, okHttpClient: OkHttpClient) : UserApiService{
//        return retrofitBuilder
//            .client(okHttpClient)
//            .build().create(UserApiService::class.java)
//    }

}