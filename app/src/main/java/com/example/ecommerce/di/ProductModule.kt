package com.example.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.data.database.CartDao
import com.example.ecommerce.data.database.CartDatabase
import com.example.ecommerce.data.network.ProductApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ProductModule {
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProductApiService {
        return retrofit.create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CartDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CartDatabase::class.java,
            "cart_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCartDao(cartDatabase: CartDatabase): CartDao {
        return cartDatabase.cartDao()
    }

}