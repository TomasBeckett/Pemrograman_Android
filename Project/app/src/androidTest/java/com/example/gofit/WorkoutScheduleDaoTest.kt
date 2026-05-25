package com.example.gofit

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.gofit.data.local.AppDatabase
import com.example.gofit.data.local.WorkoutSchedule
import com.example.gofit.data.local.WorkoutScheduleDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class WorkoutScheduleDaoTest {

    private lateinit var scheduleDao: WorkoutScheduleDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        scheduleDao = db.workoutScheduleDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndGetScheduleForDate() = runBlocking {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val schedule = WorkoutSchedule(
            workoutTitle = "Morning Run",
            date = today,
            isCompleted = false
        )

        scheduleDao.insert(schedule)
        val schedules = scheduleDao.getSchedulesForDate(today)
        
        assertEquals(1, schedules.size)
        assertEquals("Morning Run", schedules[0].workoutTitle)
    }
}
