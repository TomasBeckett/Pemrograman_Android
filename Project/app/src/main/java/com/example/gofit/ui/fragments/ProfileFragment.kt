package com.example.gofit.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gofit.R
import com.example.gofit.data.local.WorkoutLogDao
import com.example.gofit.ui.adapters.LogAdapter
import com.example.gofit.utils.BmiCalculator
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LogAdapter
    private lateinit var tvName: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvAge: TextView
    private lateinit var tvBmiScore: TextView
    private lateinit var tvBmiStatus: TextView
    private lateinit var ivProfile: ImageView
    private lateinit var btnEdit: ImageButton

    @Inject
    lateinit var workoutLogDao: WorkoutLogDao

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let { uri ->
                try {
                    requireContext().contentResolver.takePersistableUriPermission(
                        uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) { }
                
                ivProfile.setImageURI(uri)
                sharedPrefs.edit().putString("profile_image_uri", uri.toString()).apply()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initViews(view)
        setupRecyclerView()
        loadUserProfile()
        loadActivityLogs()
        return view
    }

    private fun initViews(view: View) {
        tvName = view.findViewById(R.id.profile_name)
        tvWeight = view.findViewById(R.id.tv_weight)
        tvHeight = view.findViewById(R.id.tv_height)
        tvAge = view.findViewById(R.id.tv_age)
        tvBmiScore = view.findViewById(R.id.tv_bmi_score)
        tvBmiStatus = view.findViewById(R.id.tv_bmi_status)
        ivProfile = view.findViewById(R.id.profile_image)
        btnEdit = view.findViewById(R.id.btn_edit_profile)
        recyclerView = view.findViewById(R.id.recycler_logs)

        btnEdit.setOnClickListener { showEditProfileDialog() }
        ivProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            pickImageLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = LogAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun loadUserProfile() {
        val name = sharedPrefs.getString("name", "User GoFit")
        val weight = sharedPrefs.getInt("weight", 0)
        val height = sharedPrefs.getInt("height", 0)
        val age = sharedPrefs.getInt("age", 0)

        tvName.text = name
        tvWeight.text = String.format(Locale.getDefault(), "%d kg", weight)
        tvHeight.text = String.format(Locale.getDefault(), "%d cm", height)
        tvAge.text = age.toString()
        
        updateBMI(weight, height)
        
        val imageUriString = sharedPrefs.getString("profile_image_uri", null)
        if (imageUriString != null) {
            try {
                ivProfile.setImageURI(Uri.parse(imageUriString))
            } catch (e: Exception) {
                ivProfile.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }

    private fun updateBMI(weight: Int, height: Int) {
        val bmi = BmiCalculator.calculateBmi(weight, height)
        if (bmi > 0) {
            tvBmiScore.text = String.format(Locale.getDefault(), "%.1f", bmi)
            val status = BmiCalculator.getBmiStatus(bmi)
            tvBmiStatus.text = status
            
            val color = when (status) {
                "Ideal Weight" -> "#4CAF50"
                "Underweight" -> "#03A9F4"
                "Overweight" -> "#FF9800"
                else -> "#F44336"
            }
            tvBmiStatus.setTextColor(color.toColorInt())
        } else {
            tvBmiScore.text = "0.0"
            tvBmiStatus.text = "Fill profile to calculate BMI"
            tvBmiStatus.setTextColor("#BBBBBB".toColorInt())
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_profile, null)
        val editName = dialogView.findViewById<EditText>(R.id.edit_name)
        val editWeight = dialogView.findViewById<EditText>(R.id.edit_weight)
        val editHeight = dialogView.findViewById<EditText>(R.id.edit_height)
        val editAge = dialogView.findViewById<EditText>(R.id.edit_age)

        editName.setText(sharedPrefs.getString("name", ""))
        editWeight.setText(sharedPrefs.getInt("weight", 0).toString())
        editHeight.setText(sharedPrefs.getInt("height", 0).toString())
        editAge.setText(sharedPrefs.getInt("age", 0).toString())

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Profil")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val name = editName.text.toString()
                val weight = editWeight.text.toString().toIntOrNull() ?: 0
                val height = editHeight.text.toString().toIntOrNull() ?: 0
                val age = editAge.text.toString().toIntOrNull() ?: 0

                sharedPrefs.edit().apply {
                    putString("name", name)
                    putInt("weight", weight)
                    putInt("height", height)
                    putInt("age", age)
                }.apply()
                
                loadUserProfile()
                Toast.makeText(context, "Profil Diperbarui", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun loadActivityLogs() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val logs = workoutLogDao.getAllLogs()
                adapter.updateData(logs)
            } catch (e: Exception) { }
        }
    }

    override fun onResume() {
        super.onResume()
        loadActivityLogs()
    }
}
