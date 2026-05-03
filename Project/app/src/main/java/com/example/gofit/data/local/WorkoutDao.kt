package com.example.gofit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutDao {
    @Query("SELECT * FROM workouts")
    suspend fun getAll(): List<Workout>

    @Insert
    suspend fun insert(workout: Workout)
}
