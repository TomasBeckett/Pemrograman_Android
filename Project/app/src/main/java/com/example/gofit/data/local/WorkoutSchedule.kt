package com.example.gofit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_schedules")
data class WorkoutSchedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val workoutTitle: String,
    val date: Long, // Timestamp for the scheduled day (00:00:00)
    val isCompleted: Boolean = false
)
