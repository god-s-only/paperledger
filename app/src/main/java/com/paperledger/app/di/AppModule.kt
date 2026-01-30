package com.paperledger.app.di

import android.app.Application
import androidx.room.Room
import com.paperledger.app.data.local.PaperledgerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePaperLedgerDatabase(application: Application): PaperledgerDatabase{
        return Room.databaseBuilder(
                application,
                PaperledgerDatabase::class.java,
                "paperledger_database"
            ).fallbackToDestructiveMigration(false)
            .build()
    }
}