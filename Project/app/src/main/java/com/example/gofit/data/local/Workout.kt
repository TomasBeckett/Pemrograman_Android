package com.example.gofit.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class Workout(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val type: String? = null,
    val muscle: String? = null,
    val equipment: String? = null,
    val level: String? = null,
    val instructions: String? = null
)
