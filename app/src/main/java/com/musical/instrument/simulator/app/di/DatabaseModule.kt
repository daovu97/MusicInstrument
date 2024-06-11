package com.musical.instrument.simulator.app.di

import android.content.Context
import androidx.room.Room
import com.musical.instrument.simulator.app.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDeezerDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, context.packageName)
            .build()

    @Provides
    @Singleton
    fun providesAudioDao(appDatabase: AppDatabase) = appDatabase.audioDao()

}
