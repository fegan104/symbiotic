package com.frankegan.symbiotic.di

import android.app.Application
import androidx.room.Room
import com.frankegan.symbiotic.data.SymbioticRepository
import com.frankegan.symbiotic.data.local.LocalDataSource
import com.frankegan.symbiotic.data.local.SymbioticDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [AppModule::class])
class DataModule {

    @Singleton
    @Provides
    fun provideDatabase(app: Application): SymbioticDatabase {
        return Room.databaseBuilder(app, SymbioticDatabase::class.java, "symbiotic-db").build()
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(database: SymbioticDatabase): LocalDataSource {
        return LocalDataSource(database)
    }

    @Singleton
    @Provides
    fun provideSymbioticRepository(localDataSource: LocalDataSource): SymbioticRepository {
        return SymbioticRepository(localDataSource)
    }
}