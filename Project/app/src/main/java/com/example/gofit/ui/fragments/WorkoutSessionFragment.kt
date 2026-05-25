package com.example.gofit.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.gofit.R
import com.example.gofit.data.local.WorkoutLog
import com.example.gofit.data.local.WorkoutLogDao
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class WorkoutSessionFragment : Fragment() {

    private var workoutTitle: String? = null
    private var workoutType: String? = null
    private var workoutMuscle: String? = null
    private var workoutEquipment: String? = null
    private var workoutDifficulty: String? = null
    private var workoutInstructions: String? = null

    private lateinit var chronometer: Chronometer
    private lateinit var btnStartPause: MaterialButton
    private lateinit var btnReset: MaterialButton
    private var isTimerRunning = false
    private var pauseOffset: Long = 0

    @Inject
    lateinit var workoutLogDao: WorkoutLogDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            workoutTitle = it.getString(ARG_WORKOUT_TITLE)
            workoutType = it.getString(ARG_WORKOUT_TYPE)
            workoutMuscle = it.getString(ARG_WORKOUT_MUSCLE)
            workoutEquipment = it.getString(ARG_WORKOUT_EQUIPMENT)
            workoutDifficulty = it.getString(ARG_WORKOUT_DIFFICULTY)
            workoutInstructions = it.getString(ARG_WORKOUT_INSTRUCTIONS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout_session, container, false)

        setupUI(view)
        setupTimer(view)

        return view
    }

    private fun setupUI(view: View) {
        val tvTitle = view.findViewById<TextView>(R.id.tv_session_title)
        val tvType = view.findViewById<TextView>(R.id.tv_session_type)
        val tvDifficulty = view.findViewById<TextView>(R.id.tv_session_difficulty)
        val tvMuscle = view.findViewById<TextView>(R.id.tv_session_muscle)
        val tvEquipment = view.findViewById<TextView>(R.id.tv_session_equipment)
        val tvInstructions = view.findViewById<TextView>(R.id.tv_session_instructions)
        val btnQuit = view.findViewById<ImageButton>(R.id.btn_quit_workout)
        val btnFinish = view.findViewById<Button>(R.id.btn_finish_session)

        tvTitle.text = workoutTitle?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: "Workout Session"
        tvType.text = workoutType?.uppercase() ?: "STRENGTH"
        tvDifficulty.text = workoutDifficulty?.uppercase() ?: "BEGINNER"
        tvMuscle.text = "Target: ${workoutMuscle?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "General"}"
        tvEquipment.text = "Equipment: ${workoutEquipment?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "None"}"
        
        // Memformat instruksi menjadi daftar bernomor
        tvInstructions.text = formatInstructions(workoutInstructions)

        btnQuit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        btnFinish.setOnClickListener {
            saveWorkoutAndFinish()
        }
    }

    private fun formatInstructions(rawInstructions: String?): String {
        if (rawInstructions.isNullOrBlank()) return "No instructions available for this exercise."
        
        // Memisahkan berdasarkan titik yang diikuti spasi atau baris baru
        val steps = rawInstructions.split(Regex("(?<=\\.)\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }
        
        val formatted = StringBuilder()
        steps.forEachIndexed { index, step ->
            formatted.append("${index + 1}. $step\n\n")
        }
        
        return formatted.toString().trim()
    }

    private fun setupTimer(view: View) {
        chronometer = view.findViewById(R.id.workout_chronometer)
        btnStartPause = view.findViewById(R.id.btn_timer_start_pause)
        btnReset = view.findViewById(R.id.btn_timer_reset)

        btnStartPause.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        btnReset.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        chronometer.base = SystemClock.elapsedRealtime() - pauseOffset
        chronometer.start()
        isTimerRunning = true
        btnStartPause.setIconResource(android.R.drawable.ic_media_pause)
        btnStartPause.setBackgroundColor(Color.parseColor("#F44336")) // Merah untuk Pause
    }

    private fun pauseTimer() {
        chronometer.stop()
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.base
        isTimerRunning = false
        btnStartPause.setIconResource(android.R.drawable.ic_media_play)
        btnStartPause.setBackgroundColor(Color.parseColor("#BB86FC")) // Warna Utama untuk Start
    }

    private fun resetTimer() {
        chronometer.stop()
        chronometer.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
        isTimerRunning = false
        btnStartPause.setIconResource(android.R.drawable.ic_media_play)
        btnStartPause.setBackgroundColor(Color.parseColor("#BB86FC"))
        Toast.makeText(context, "Timer Reset", Toast.LENGTH_SHORT).show()
    }

    private fun saveWorkoutAndFinish() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val log = WorkoutLog(
                    workoutTitle = workoutTitle ?: "Unknown Workout",
                    caloriesBurned = (150..400).random()
                )
                workoutLogDao.insertLog(log)
                Toast.makeText(context, "Workout Selesai & Dicatat!", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Gagal menyimpan progress", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val ARG_WORKOUT_TITLE = "workout_title"
        private const val ARG_WORKOUT_TYPE = "workout_type"
        private const val ARG_WORKOUT_MUSCLE = "workout_muscle"
        private const val ARG_WORKOUT_EQUIPMENT = "workout_equipment"
        private const val ARG_WORKOUT_DIFFICULTY = "workout_difficulty"
        private const val ARG_WORKOUT_INSTRUCTIONS = "workout_instructions"

        fun newInstance(
            title: String,
            type: String? = null,
            muscle: String? = null,
            equipment: String? = null,
            difficulty: String? = null,
            instructions: String? = null
        ) = WorkoutSessionFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_WORKOUT_TITLE, title)
                putString(ARG_WORKOUT_TYPE, type)
                putString(ARG_WORKOUT_MUSCLE, muscle)
                putString(ARG_WORKOUT_EQUIPMENT, equipment)
                putString(ARG_WORKOUT_DIFFICULTY, difficulty)
                putString(ARG_WORKOUT_INSTRUCTIONS, instructions)
            }
        }
    }
}
