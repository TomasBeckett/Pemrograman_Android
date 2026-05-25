package com.example.gofit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.remote.Muscle
import com.example.gofit.ui.adapters.MuscleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MuscleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)

        recyclerView = view.findViewById(R.id.recycler_muscles)
        progressBar = view.findViewById(R.id.progress_bar_schedule)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MuscleAdapter(emptyList())
        recyclerView.adapter = adapter

        loadMuscles()

        return view
    }

    private fun loadMuscles() {
        progressBar.visibility = View.VISIBLE
        
        // Daftar otot yang didukung oleh API Ninjas
        val muscleList = listOf(
            Muscle(1, "abdominals"),
            Muscle(2, "abductors"),
            Muscle(3, "adductors"),
            Muscle(4, "biceps"),
            Muscle(5, "calves"),
            Muscle(6, "chest"),
            Muscle(7, "forearms"),
            Muscle(8, "glutes"),
            Muscle(9, "hamstrings"),
            Muscle(10, "lats"),
            Muscle(11, "lower_back"),
            Muscle(12, "middle_back"),
            Muscle(13, "neck"),
            Muscle(14, "quadriceps"),
            Muscle(15, "traps"),
            Muscle(16, "triceps")
        )
        
        adapter.updateData(muscleList)
        progressBar.visibility = View.GONE
    }
}
