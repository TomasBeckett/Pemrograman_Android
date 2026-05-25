package com.example.gofit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.data.local.WorkoutLog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogAdapter(private var logs: List<WorkoutLog>) :
    RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    class LogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(android.R.id.text1)
        val detail: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return LogViewHolder(view)
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val log = logs[position]
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val dateString = sdf.format(Date(log.timestamp))

        holder.title.text = log.workoutTitle
        holder.title.setTextColor(android.graphics.Color.WHITE)
        
        holder.detail.text = "$dateString • ${log.caloriesBurned} kcal"
        holder.detail.setTextColor(android.graphics.Color.LTGRAY)
    }

    override fun getItemCount() = logs.size

    fun updateData(newLogs: List<WorkoutLog>) {
        logs = newLogs
        notifyDataSetChanged()
    }
}
