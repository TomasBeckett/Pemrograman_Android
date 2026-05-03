package com.example.gofit.ui.adapters

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.remote.RemoteWorkout

class WorkoutAdapter(private var workouts: List<RemoteWorkout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    class WorkoutViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.workout_title)
        val description: TextView = view.findViewById(R.id.workout_duration)
        val level: TextView = view.findViewById(R.id.workout_level)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        
        // Handling nullable fields to prevent crash
        holder.title.text = workout.name ?: "No Title"
        
        val desc = workout.description ?: ""
        if (desc.isNotEmpty()) {
            holder.description.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)
        } else {
            holder.description.text = "No description available"
        }
        
        holder.level.text = "ID: ${workout.id ?: "N/A"}"
    }

    override fun getItemCount() = workouts.size

    fun updateData(newWorkouts: List<RemoteWorkout>?) {
        workouts = newWorkouts ?: emptyList()
        notifyDataSetChanged()
    }
}
