package com.paperledger.app.di

import android.content.Context
import com.netscope.interceptor.NetScopeInterceptor
import com.paperledger.app.BuildConfig
import com.paperledger.app.core.BASE_URL
import com.paperledger.app.core.alpacaAuthHeader
import com.paperledger.app.data.remote.api.AlpacaApiService
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideAlpacaApiService(client: OkHttpClient): AlpacaApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(AlpacaApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader(
                        "Authorization",
                        alpacaAuthHeader(BuildConfig.ALPACA_API_KEY, BuildConfig.ALPACA_API_SECRET)
                    )
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(NetScopeInterceptor(context))
            .build()
    }
}