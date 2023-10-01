package com.example.ecommerce.di

import android.content.Context
import androidx.room.Room
import com.example.ecommerce.R
import com.example.ecommerce.data.database.cart.CartDao
import com.example.ecommerce.data.database.cart.CartDatabase
import com.example.ecommerce.data.database.notification.NotificationDao
import com.example.ecommerce.data.database.wishlist.WishlistDao
import com.example.ecommerce.data.network.ProductApiService
import com.example.ecommerce.data.repository.NotificationRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
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

    @Provides
    @Singleton
    fun provideNotificationDao(cartDatabase: CartDatabase): NotificationDao {
        return cartDatabase.notificationDao()
    }

    @Provides
    @Singleton
    fun provideRepositoryNotification(notificationDao: NotificationDao) =
        NotificationRepository(notificationDao)

    @Provides
    @Singleton
    fun provideWishlistDao(cartDatabase: CartDatabase): WishlistDao {
        return cartDatabase.wishlistDao()
    }

    @Provides
    @Singleton
    fun firebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        return remoteConfig
    }

    @Provides
    @Singleton
    fun firebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics =
        Firebase.analytics

}