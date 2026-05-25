package com.example.gofit.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutScheduleDao {
    @Query("SELECT * FROM workout_schedules WHERE date = :date")
    suspend fun getSchedulesForDate(date: Long): List<WorkoutSchedule>

    @Insert
    suspend fun insert(schedule: WorkoutSchedule)

    @Update
    suspend fun update(schedule: WorkoutSchedule)

    @Delete
    suspend fun delete(schedule: WorkoutSchedule)

    @Query("SELECT * FROM workout_schedules ORDER BY date ASC")
    suspend fun getAllSchedules(): List<WorkoutSchedule>
}
