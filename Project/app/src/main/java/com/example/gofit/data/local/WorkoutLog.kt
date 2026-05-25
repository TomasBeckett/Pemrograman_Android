package com.example.gofit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_logs")
data class WorkoutLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutTitle: String,
    val timestamp: Long = System.currentTimeMillis(),
    val caloriesBurned: Int = 100 // Angka dummy untuk simulasi progress
)
