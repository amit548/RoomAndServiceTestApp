package com.example.rommservicetestapp

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): CounterDatabase = Room.databaseBuilder(
        context,
        CounterDatabase::class.java,
        "counter.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCounterDao(
        database: CounterDatabase
    ): CounterDao = database.counterDao()
}