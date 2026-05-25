package com.example.gofit

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GoFitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d("GoFitDebug", "Aplikasi GoFit Berhasil Dijalankan!")
    }
}
