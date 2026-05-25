package com.example.gofit.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.remote.RemoteWorkout
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class WorkoutAdapter(
    private var originalWorkouts: List<RemoteWorkout>,
    private val onAddClick: (RemoteWorkout) -> Unit,
    private val onPlayClick: (RemoteWorkout) -> Unit,
    private val onScheduleClick: (RemoteWorkout) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    private var filteredWorkouts: List<RemoteWorkout> = originalWorkouts

    class WorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.workout_title)
        val muscle: TextView = view.findViewById(R.id.workout_muscle)
        val type: TextView = view.findViewById(R.id.workout_type)
        val equipment: TextView = view.findViewById(R.id.workout_equipment)
        val level: TextView = view.findViewById(R.id.workout_level)
        val levelContainer: MaterialCardView = view.findViewById(R.id.workout_level_container)
        
        val addButton: ImageButton = view.findViewById(R.id.btn_add_favorite)
        val playButton: MaterialButton = view.findViewById(R.id.btn_play_workout)
        val scheduleButton: ImageButton = view.findViewById(R.id.btn_schedule_workout)

        init {
            view.findViewById<View>(R.id.btn_complete_workout).visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = filteredWorkouts[position]
        
        // Title formatting
        holder.title.text = workout.name?.split(" ")?.joinToString(" ") { 
            it.replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() } 
        } ?: "No Title"
        
        // Muscle and Type
        holder.muscle.text = workout.muscle?.replace("_", " ")?.uppercase() ?: "GENERAL"
        holder.type.text = workout.type?.replace("_", " ")?.uppercase() ?: "STRENGTH"
        
        // Equipment
        val equipText = workout.equipment?.replace("_", " ")?.replaceFirstChar { it.uppercase() } ?: "None"
        holder.equipment.text = "Equipment: $equipText"
        
        // Difficulty Badge Logic
        val difficulty = workout.difficulty?.lowercase() ?: "beginner"
        holder.level.text = difficulty.uppercase()
        
        val color = when (difficulty) {
            "beginner" -> "#4CAF50" // Green
            "intermediate" -> "#FF9800" // Orange
            "expert" -> "#F44336" // Red
            else -> "#4CAF50"
        }
        holder.levelContainer.setCardBackgroundColor(Color.parseColor(color))

        // Button Click Listeners
        holder.addButton.setOnClickListener { onAddClick(workout) }
        holder.playButton.setOnClickListener { onPlayClick(workout) }
        holder.scheduleButton.setOnClickListener { onScheduleClick(workout) }
    }

    override fun getItemCount() = filteredWorkouts.size

    fun updateData(newWorkouts: List<RemoteWorkout>?) {
        originalWorkouts = newWorkouts ?: emptyList()
        filteredWorkouts = originalWorkouts
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        filteredWorkouts = if (query.isEmpty()) {
            originalWorkouts
        } else {
            val lowerQuery = query.lowercase(Locale.getDefault())
            originalWorkouts.filter {
                it.name?.lowercase(Locale.getDefault())?.contains(lowerQuery) == true ||
                it.muscle?.lowercase(Locale.getDefault())?.contains(lowerQuery) == true ||
                it.type?.lowercase(Locale.getDefault())?.contains(lowerQuery) == true
            }
        }
        notifyDataSetChanged()
    }
}
