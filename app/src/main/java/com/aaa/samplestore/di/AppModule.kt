package com.aaa.samplestore.di

import android.content.Context
import com.aaa.samplestore.common.Constants
import com.aaa.samplestore.data.local.dao.CartDao
import com.aaa.samplestore.data.local.dao.UserDao
import com.aaa.samplestore.data.local.dao.WishlistDao
import com.aaa.samplestore.data.local.database.AppDatabase
import com.aaa.samplestore.data.local.sharedpreference.EncryptedSharedPreferenceManager
import com.aaa.samplestore.data.local.sharedpreference.SessionManager
import com.aaa.samplestore.data.remote.FakeStoreApi
import com.aaa.samplestore.data.remote.PayPalApi
import com.aaa.samplestore.data.remote.interceptors.CurlLogInterceptor
import com.aaa.samplestore.data.repository.CartRepository
import com.aaa.samplestore.data.repository.CheckOutRepository
import com.aaa.samplestore.data.repository.ProductRepository
import com.aaa.samplestore.data.repository.UserRepository
import com.aaa.samplestore.domain.repository.ICartRepository
import com.aaa.samplestore.domain.repository.ICheckOutRepository
import com.aaa.samplestore.domain.repository.IProductRepository
import com.aaa.samplestore.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApi(): FakeStoreApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(CurlLogInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder)
            .build()
            .create(FakeStoreApi::class.java)
    }

    @Provides
    @Singleton
    fun providePayPalApi(): PayPalApi {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(CurlLogInterceptor())
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.PAYPAL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder)
            .build()
        return retrofit.create(PayPalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun provideCartDao(appDatabase: AppDatabase): CartDao = appDatabase.cartDao()

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideWishlistDao(appDatabase: AppDatabase): WishlistDao = appDatabase.wishlistDao()

    @Provides
    @Singleton
    fun provideProductRepository(api: FakeStoreApi): IProductRepository = ProductRepository(api)

    @Provides
    @Singleton
    fun provideUserRepository(api: FakeStoreApi): IUserRepository = UserRepository(api)

    @Provides
    @Singleton
    fun provideCartRepository(cartDao: CartDao): ICartRepository = CartRepository(cartDao)

    @Provides
    @Singleton
    fun provideCheckoutRepository(api: PayPalApi): ICheckOutRepository = CheckOutRepository(api)

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager = SessionManager(context)

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferenceManager(@ApplicationContext context: Context): EncryptedSharedPreferenceManager = EncryptedSharedPreferenceManager(context)
}