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
import com.example.gofit.ui.adapters.MuscleAdapter
import kotlinx.coroutines.launch

class ScheduleFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: MuscleAdapter
    private val apiService by lazy { ApiService.create() }

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

        fetchMuscles()

        return view
    }

    private fun fetchMuscles() {
        progressBar.visibility = View.VISIBLE
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = apiService.getMuscles()
                adapter.updateData(response.results)
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
