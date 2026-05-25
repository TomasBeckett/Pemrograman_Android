package com.example.gofit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gofit.data.local.AppDatabase
import com.example.gofit.data.local.Workout
import com.example.gofit.data.local.WorkoutDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class WorkoutDaoTest {

    private lateinit var workoutDao: WorkoutDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        workoutDao = db.workoutDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeWorkoutAndReadInList() = runBlocking {
        val workout = Workout(
            id = 1,
            title = "Push Up",
            muscle = "Chest",
            level = "Beginner"
        )
        workoutDao.insert(workout)
        val allWorkouts = workoutDao.getAll()
        assertEquals(allWorkouts[0].title, workout.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteWorkout_removesFromList() = runBlocking {
        val workout = Workout(id = 2, title = "Squat")
        workoutDao.insert(workout)
        workoutDao.delete(workout)
        val allWorkouts = workoutDao.getAll()
        assertEquals(0, allWorkouts.size)
    }
}
