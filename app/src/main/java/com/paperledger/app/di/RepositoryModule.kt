package com.paperledger.app.di

import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.repository.ACHRelationshipRepositoryImpl
import com.paperledger.app.data.repository.AssetsRepositoryImpl
import com.paperledger.app.data.repository.AuthRepositoryImpl
import com.paperledger.app.data.repository.FundingRepositoryImpl
import com.paperledger.app.data.repository.WatchlistsRepositoryImpl
import com.paperledger.app.domain.repository.ACHRelationshipRepository
import com.paperledger.app.domain.repository.AssetsRepository
import com.paperledger.app.domain.repository.AuthRepository
import com.paperledger.app.domain.repository.FundingRepository
import com.paperledger.app.domain.repository.WatchlistsRepository
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

    @Provides
    @Singleton
    fun provideACHRelationshipRepository(alpacaApi: AlpacaApiService, paperLedgerSession: PaperLedgerSession): ACHRelationshipRepository{
        return ACHRelationshipRepositoryImpl(alpacaApi, paperLedgerSession)
    }

    @Provides
    @Singleton
    fun provideFundingRepository(alpacaApi: AlpacaApiService): FundingRepository {
        return FundingRepositoryImpl(alpacaApi)
    }

    @Provides
    @Singleton
    fun provideAssetsRepository(alpacaApi: AlpacaApiService): AssetsRepository {
        return AssetsRepositoryImpl(alpacaApi)
    }

    @Provides
    @Singleton
    fun provideWatchlistsRepository(alpacaApi: AlpacaApiService): WatchlistsRepository {
        return WatchlistsRepositoryImpl(alpacaApi)
    }
}