package com.paperledger.app.di

import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.repository.AuthRepositoryImpl
import com.paperledger.app.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(alpacaApi: AlpacaApiService, paperLedgerSession: PaperLedgerSession): AuthRepository{
        return AuthRepositoryImpl(alpacaApi, paperLedgerSession)
    }
}