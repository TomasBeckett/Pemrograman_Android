package com.example.gofit.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.data.remote.Muscle

class MuscleAdapter(private var muscles: List<Muscle>) :
    RecyclerView.Adapter<MuscleAdapter.MuscleViewHolder>() {

    class MuscleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MuscleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val muscle = muscles[position]
        holder.name.text = muscle.name
        holder.name.setTextColor(android.graphics.Color.WHITE)
    }

    override fun getItemCount() = muscles.size

    fun updateData(newMuscles: List<Muscle>) {
        muscles = newMuscles
        notifyDataSetChanged()
    }
}
