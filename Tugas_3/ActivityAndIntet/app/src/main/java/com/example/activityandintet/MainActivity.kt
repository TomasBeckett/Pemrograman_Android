package com.example.activityandintet

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnForm = findViewById<Button>(R.id.btnForm)
        val btnLinkedin = findViewById<Button>(R.id.btnLinkedin)

        btnForm.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        btnLinkedin.setOnClickListener {
            val url = "https://www.linkedin.com/in/tomas-becket-5538213b7/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}