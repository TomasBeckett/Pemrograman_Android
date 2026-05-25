package com.example.gofit.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.local.Workout
import com.example.gofit.data.local.WorkoutDao
import com.example.gofit.data.local.WorkoutLog
import com.example.gofit.data.local.WorkoutLogDao
import com.example.gofit.data.local.WorkoutSchedule
import com.example.gofit.data.local.WorkoutScheduleDao
import com.example.gofit.ui.adapters.FavoriteWorkoutAdapter
import com.example.gofit.ui.adapters.ScheduledWorkoutAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = "GoFitDebug"

    private lateinit var recyclerFavorites: RecyclerView
    private lateinit var favoriteAdapter: FavoriteWorkoutAdapter
    private lateinit var recyclerScheduled: RecyclerView
    private lateinit var scheduledAdapter: ScheduledWorkoutAdapter
    private lateinit var tvTotalWorkouts: TextView
    private lateinit var tvTotalCalories: TextView
    private lateinit var tvNoSchedule: TextView
    private lateinit var calendarView: CalendarView
    private lateinit var dayViews: List<View>

    private var selectedDateMillis: Long = 0

    @Inject
    lateinit var workoutDao: WorkoutDao
    @Inject
    lateinit var workoutLogDao: WorkoutLogDao
    @Inject
    lateinit var workoutScheduleDao: WorkoutScheduleDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "HomeFragment: Memuat tampilan")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        initViews(view)
        setupRecyclerViews()
        setupCalendar()
        loadData()

        return view
    }

    private fun initViews(view: View) {
        tvTotalWorkouts = view.findViewById(R.id.tv_total_workouts)
        tvTotalCalories = view.findViewById(R.id.tv_total_calories)
        tvNoSchedule = view.findViewById(R.id.tv_no_schedule)
        calendarView = view.findViewById(R.id.calendar_view)
        recyclerFavorites = view.findViewById(R.id.recycler_favorites)
        recyclerScheduled = view.findViewById(R.id.recycler_scheduled_workouts)
        
        dayViews = listOf(
            view.findViewById(R.id.day_mon), view.findViewById(R.id.day_tue),
            view.findViewById(R.id.day_wed), view.findViewById(R.id.day_thu),
            view.findViewById(R.id.day_fri), view.findViewById(R.id.day_sat),
            view.findViewById(R.id.day_sun)
        )
    }

    private fun setupRecyclerViews() {
        favoriteAdapter = FavoriteWorkoutAdapter(
            workouts = emptyList(),
            onPlayClick = { workout -> startWorkoutSession(workout) },
            onDeleteClick = { workout -> deleteFavorite(workout) }
        )
        recyclerFavorites.layoutManager = LinearLayoutManager(context)
        recyclerFavorites.adapter = favoriteAdapter

        scheduledAdapter = ScheduledWorkoutAdapter(
            schedules = emptyList(),
            onDeleteClick = { schedule -> deleteSchedule(schedule) }
        )
        recyclerScheduled.layoutManager = LinearLayoutManager(context)
        recyclerScheduled.adapter = scheduledAdapter
    }

    private fun setupCalendar() {
        val today = Calendar.getInstance()
        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)
        selectedDateMillis = today.timeInMillis

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth, 0, 0, 0)
            cal.set(Calendar.MILLISECOND, 0)
            selectedDateMillis = cal.timeInMillis
            Log.d(TAG, "HomeFragment: Tanggal dipilih: ${cal.time}")
            loadSchedulesForDate()
        }
    }

    private fun loadData() {
        Log.d(TAG, "HomeFragment: Menyegarkan data...")
        loadFavorites()
        loadProgressStats()
        loadWeeklyProgress()
        loadSchedulesForDate()
    }

    private fun startWorkoutSession(workout: Workout) {
        Log.d(TAG, "HomeFragment: Memulai sesi favorit: ${workout.title}")
        val sessionFragment = WorkoutSessionFragment.newInstance(
            title = workout.title,
            type = workout.type,
            muscle = workout.muscle,
            equipment = workout.equipment,
            difficulty = workout.level,
            instructions = workout.instructions
        )
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, sessionFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun deleteSchedule(schedule: WorkoutSchedule) {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d(TAG, "HomeFragment: Menghapus jadwal: ${schedule.workoutTitle}")
            workoutScheduleDao.delete(schedule)
            loadSchedulesForDate()
        }
    }

    private fun deleteFavorite(workout: Workout) {
        viewLifecycleOwner.lifecycleScope.launch {
            Log.d(TAG, "HomeFragment: Menghapus favorit: ${workout.title}")
            workoutDao.delete(workout)
            loadFavorites()
        }
    }

    private fun loadSchedulesForDate() {
        viewLifecycleOwner.lifecycleScope.launch {
            val schedules = workoutScheduleDao.getSchedulesForDate(selectedDateMillis)
            Log.d(TAG, "HomeFragment: Ditemukan ${schedules.size} jadwal untuk tanggal ini")
            scheduledAdapter.updateData(schedules)
            tvNoSchedule.visibility = if (schedules.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun loadFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            val favorites = workoutDao.getAll()
            Log.d(TAG, "HomeFragment: Memuat ${favorites.size} latihan favorit")
            favoriteAdapter.updateData(favorites)
        }
    }

    private fun loadProgressStats() {
        viewLifecycleOwner.lifecycleScope.launch {
            val total = workoutLogDao.getTotalWorkouts()
            val calories = workoutLogDao.getTotalCalories() ?: 0
            tvTotalWorkouts.text = total.toString()
            tvTotalCalories.text = calories.toString()
        }
    }

    private fun loadWeeklyProgress() {
        viewLifecycleOwner.lifecycleScope.launch {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_YEAR, -6)
            }
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val currentCalendar = calendar.clone() as Calendar
            for (i in 0..6) {
                val startTime = currentCalendar.timeInMillis
                val endTime = startTime + (24 * 60 * 60 * 1000) - 1
                val count = workoutLogDao.getWorkoutCountInRange(startTime, endTime)
                updateBarHeight(dayViews[i], count)
                currentCalendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }

    private fun updateBarHeight(dayView: View, count: Int) {
        val bar = dayView.findViewById<View>(R.id.view_bar)
        val params = bar.layoutParams
        val density = resources.displayMetrics.density
        params.height = ((4 + (count * 15)) * density).toInt().coerceAtMost((60 * density).toInt())
        bar.setBackgroundColor(resources.getColor(if (count > 0) android.R.color.holo_green_light else android.R.color.darker_gray, null))
        bar.layoutParams = params
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}
