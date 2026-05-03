package com.example.gofit.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.remote.ApiService
import com.example.gofit.ui.adapters.WorkoutAdapter
import kotlinx.coroutines.launch

class WorkoutFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: WorkoutAdapter
    private val apiService by lazy { ApiService.create() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)
        
        recyclerView = view.findViewById(R.id.recycler_workout)
        progressBar = view.findViewById(R.id.progress_bar)
        
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = WorkoutAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchWorkouts()

        return view
    }

    private fun fetchWorkouts() {
        progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getExercises()
                adapter.updateData(response.results)
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
