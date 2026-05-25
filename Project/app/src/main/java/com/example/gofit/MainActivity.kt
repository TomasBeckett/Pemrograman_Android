package com.example.gofit

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.gofit.ui.fragments.HomeFragment
import com.example.gofit.ui.fragments.ProfileFragment
import com.example.gofit.ui.fragments.WorkoutFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG = "GoFitDebug"
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity: onCreate - App Started")

        bottomNav = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment(), "HOME")
        }

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), "HOME")
                    true
                }
                R.id.nav_workout -> {
                    loadFragment(WorkoutFragment(), "WORKOUT")
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(), "PROFILE")
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, name: String) {
        Log.i(TAG, "MainActivity: Navigating to $name")
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_container, fragment)
            .commit()
    }
}
