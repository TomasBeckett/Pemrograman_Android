package com.example.gofit.di

import android.content.Context
import com.example.gofit.data.local.AppDatabase
import com.example.gofit.data.local.WorkoutDao
import com.example.gofit.data.local.WorkoutLogDao
import com.example.gofit.data.local.WorkoutScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideWorkoutDao(database: AppDatabase): WorkoutDao {
        return database.workoutDao()
    }

    @Provides
    fun provideWorkoutLogDao(database: AppDatabase): WorkoutLogDao {
        return database.workoutLogDao()
    }

    @Provides
    fun provideWorkoutScheduleDao(database: AppDatabase): WorkoutScheduleDao {
        return database.workoutScheduleDao()
    }
}
