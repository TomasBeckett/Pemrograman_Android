package com.example.gofit.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.local.Workout
import com.google.android.material.card.MaterialCardView
import java.util.Locale

class FavoriteWorkoutAdapter(
    private var workouts: List<Workout>,
    private val onPlayClick: (Workout) -> Unit,
    private val onDeleteClick: (Workout) -> Unit
) : RecyclerView.Adapter<FavoriteWorkoutAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.workout_title)
        val muscle: TextView = view.findViewById(R.id.workout_muscle)
        val level: TextView = view.findViewById(R.id.workout_level)
        val levelContainer: MaterialCardView = view.findViewById(R.id.workout_level_container)
        val btnPlay: View = view.findViewById(R.id.btn_play_workout)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_add_favorite)
        
        init {
            view.findViewById<View>(R.id.workout_type).visibility = View.GONE
            view.findViewById<View>(R.id.workout_equipment).visibility = View.GONE
            view.findViewById<View>(R.id.btn_schedule_workout).visibility = View.GONE
            view.findViewById<View>(R.id.btn_complete_workout).visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val workout = workouts[position]
        holder.title.text = workout.title
        
        holder.muscle.text = workout.muscle?.replace("_", " ")?.uppercase(Locale.getDefault()) ?: "GENERAL"
        holder.level.text = workout.level?.uppercase(Locale.getDefault()) ?: "BEGINNER"
        
        val difficulty = workout.level?.lowercase(Locale.getDefault()) ?: "beginner"
        val color = when {
            difficulty.contains("beginner") -> "#4CAF50"
            difficulty.contains("intermediate") -> "#FF9800"
            difficulty.contains("expert") -> "#F44336"
            else -> "#4CAF50"
        }
        holder.levelContainer.setCardBackgroundColor(Color.parseColor(color))

        holder.btnDelete.setImageResource(android.R.drawable.ic_menu_delete)

        holder.btnPlay.setOnClickListener { onPlayClick(workout) }
        holder.btnDelete.setOnClickListener { onDeleteClick(workout) }
    }

    override fun getItemCount() = workouts.size

    fun updateData(newWorkouts: List<Workout>) {
        workouts = newWorkouts
        notifyDataSetChanged()
    }
}
