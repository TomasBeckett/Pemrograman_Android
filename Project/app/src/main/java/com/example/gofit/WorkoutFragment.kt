package com.example.gofit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutFragment : Fragment() {

    private lateinit var adapter: WorkoutAdapter
    private var workoutList = mutableListOf<Workout>()
    private lateinit var database: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        
        database = AppDatabase.getDatabase(requireContext())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_workout)
        adapter = WorkoutAdapter(workoutList)
        recyclerView.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fab_add_workout)
        fab.setOnClickListener {
            showAddWorkoutDialog()
        }

        loadWorkouts()

        return view
    }

    private fun loadWorkouts() {
        lifecycleScope.launch {
            val workouts = withContext(Dispatchers.IO) {
                database.workoutDao().getAll()
            }
            workoutList.clear()
            workoutList.addAll(workouts)
            adapter.notifyDataSetChanged()
        }
    }

    private fun showAddWorkoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tambah Latihan Baru")

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_workout, null)
        val inputTitle = dialogView.findViewById<EditText>(R.id.edit_title)
        val inputDuration = dialogView.findViewById<EditText>(R.id.edit_duration)
        val inputLevel = dialogView.findViewById<EditText>(R.id.edit_level)

        builder.setView(dialogView)
        builder.setPositiveButton("Simpan") { _, _ ->
            val title = inputTitle.text.toString()
            val duration = inputDuration.text.toString()
            val level = inputLevel.text.toString()

            if (title.isNotEmpty()) {
                val newWorkout = Workout(
                    title = title,
                    duration = duration,
                    level = level
                )
                
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        database.workoutDao().insert(newWorkout)
                    }
                    loadWorkouts() // Refresh data dari database
                }
            }
        }
        builder.setNegativeButton("Batal", null)
        builder.show()
    }
}