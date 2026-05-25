package com.example.gofit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_logs ORDER BY timestamp DESC")
    suspend fun getAllLogs(): List<WorkoutLog>

    @Insert
    suspend fun insertLog(log: WorkoutLog)

    @Query("SELECT COUNT(*) FROM workout_logs")
    suspend fun getTotalWorkouts(): Int

    @Query("SELECT SUM(caloriesBurned) FROM workout_logs")
    suspend fun getTotalCalories(): Int?

    @Query("SELECT COUNT(*) FROM workout_logs WHERE timestamp >= :startTime AND timestamp <= :endTime")
    suspend fun getWorkoutCountInRange(startTime: Long, endTime: Long): Int
}
