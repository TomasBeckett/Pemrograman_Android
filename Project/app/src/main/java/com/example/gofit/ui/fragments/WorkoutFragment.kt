package com.example.gofit.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
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
import com.example.gofit.data.remote.ApiService
import com.example.gofit.data.remote.RemoteWorkout
import com.example.gofit.ui.adapters.WorkoutAdapter
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutFragment : Fragment() {

    private val TAG = "GoFitDebug"

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: WorkoutAdapter
    private lateinit var searchEditText: EditText
    private lateinit var chipGroupFilter: ChipGroup
    private var searchJob: Job? = null

    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var workoutDao: WorkoutDao
    @Inject
    lateinit var workoutLogDao: WorkoutLogDao
    @Inject
    lateinit var workoutScheduleDao: WorkoutScheduleDao

    private val API_KEY = "Puc09JxNIhYdPAhKb8VSBwJaNll8v19Yeng8al4O"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "WorkoutFragment: onCreateView started")
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        
        initViews(view)
        setupRecyclerView()
        setupSearch()
        setupChips()
        
        fetchWorkouts(muscle = "chest") 

        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler_workout)
        progressBar = view.findViewById(R.id.progress_bar)
        searchEditText = view.findViewById(R.id.et_search_workout)
        chipGroupFilter = view.findViewById(R.id.chip_group_filter)
    }

    private fun setupRecyclerView() {
        adapter = WorkoutAdapter(
            emptyList(),
            onAddClick = { remoteWorkout -> 
                Log.d(TAG, "Adding favorite: ${remoteWorkout.name}")
                saveToFavorite(remoteWorkout) 
            },
            onPlayClick = { remoteWorkout -> 
                Log.d(TAG, "Starting session: ${remoteWorkout.name}")
                startWorkoutSession(remoteWorkout) 
            },
            onScheduleClick = { remoteWorkout -> 
                scheduleWorkout(remoteWorkout.name ?: "No Name") 
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupChips() {
        chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                fetchWorkouts(muscle = "chest")
                return@setOnCheckedStateChangeListener
            }

            val id = checkedIds.first()
            Log.d(TAG, "Chip selected: $id")
            when (id) {
                R.id.chip_chest -> fetchWorkouts(muscle = "chest")
                R.id.chip_biceps -> fetchWorkouts(muscle = "biceps")
                R.id.chip_triceps -> fetchWorkouts(muscle = "triceps")
                R.id.chip_cardio -> fetchWorkouts(type = "cardio")
                R.id.chip_stretching -> fetchWorkouts(type = "stretching")
                R.id.chip_abs -> fetchWorkouts(muscle = "abdominals")
            }
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(600)
                    val query = s.toString().trim()
                    if (query.isNotEmpty()) {
                        Log.i(TAG, "User searching for: $query")
                        chipGroupFilter.clearCheck()
                        fetchWorkouts(name = query)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun fetchWorkouts(name: String? = null, muscle: String? = null, type: String? = null) {
        Log.d(TAG, "fetchWorkouts: name=$name, muscle=$muscle, type=$type")
        progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val exercises = apiService.getExercises(apiKey = API_KEY, name = name, muscle = muscle, type = type)
                Log.i(TAG, "API Success: Received ${exercises.size} exercises")
                adapter.updateData(exercises)
                progressBar.visibility = View.GONE
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string() ?: "Bad Request"
                Log.e(TAG, "API Error: $errorBody")
                progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "API Error", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "System Error: ${e.message}")
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun startWorkoutSession(remote: RemoteWorkout) {
        val sessionFragment = WorkoutSessionFragment.newInstance(
            title = remote.name ?: "No Name",
            type = remote.type,
            muscle = remote.muscle,
            equipment = remote.equipment,
            difficulty = remote.difficulty,
            instructions = remote.instructions
        )
        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_container, sessionFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun scheduleWorkout(title: String) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Pilih Tanggal")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val utcCal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = selection }
            val localCal = Calendar.getInstance().apply {
                set(utcCal.get(Calendar.YEAR), utcCal.get(Calendar.MONTH), utcCal.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
                set(Calendar.MILLISECOND, 0)
            }
            Log.d(TAG, "Scheduling $title at ${localCal.time}")
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val schedule = WorkoutSchedule(workoutTitle = title, date = localCal.timeInMillis)
                    workoutScheduleDao.insert(schedule)
                    Toast.makeText(requireContext(), "$title dijadwalkan!", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e(TAG, "Schedule failed: ${e.message}")
                }
            }
        }
        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun saveToFavorite(remote: RemoteWorkout) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val workout = Workout(
                    title = remote.name ?: "No Name",
                    type = remote.type,
                    muscle = remote.muscle,
                    equipment = remote.equipment,
                    level = remote.difficulty,
                    instructions = remote.instructions
                )
                workoutDao.insert(workout)
                Log.i(TAG, "Favorite saved: ${remote.name}")
                Toast.makeText(requireContext(), "${remote.name} favorit!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Save failed")
            }
        }
    }
}
