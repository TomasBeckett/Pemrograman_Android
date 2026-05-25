package com.example.gofit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.local.WorkoutSchedule

class ScheduledWorkoutAdapter(
    private var schedules: List<WorkoutSchedule>,
    private val onDeleteClick: (WorkoutSchedule) -> Unit
) : RecyclerView.Adapter<ScheduledWorkoutAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.workout_title)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_add_favorite) // Reuse as delete
        
        init {
            // Sembunyikan elemen yang tidak diperlukan untuk tampilan jadwal
            view.findViewById<View>(R.id.btn_play_workout).visibility = View.GONE
            view.findViewById<View>(R.id.btn_schedule_workout).visibility = View.GONE
            view.findViewById<View>(R.id.btn_complete_workout).visibility = View.GONE
            view.findViewById<View>(R.id.workout_muscle).visibility = View.GONE
            view.findViewById<View>(R.id.workout_type).visibility = View.GONE
            view.findViewById<View>(R.id.workout_equipment).visibility = View.GONE
            view.findViewById<View>(R.id.workout_level_container).visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.title.text = schedule.workoutTitle
        holder.btnDelete.setImageResource(android.R.drawable.ic_menu_delete)
        
        if (schedule.isCompleted) {
            holder.title.paintFlags = holder.title.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            holder.title.alpha = 0.6f
        } else {
            holder.title.paintFlags = holder.title.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.title.alpha = 1.0f
        }

        holder.btnDelete.setOnClickListener { onDeleteClick(schedule) }
    }

    override fun getItemCount() = schedules.size

    fun updateData(newSchedules: List<WorkoutSchedule>) {
        schedules = newSchedules
        notifyDataSetChanged()
    }
}
