package com.example.activityandintet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class FormActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val etNim = findViewById<EditText>(R.id.etNim)
        val etNama = findViewById<EditText>(R.id.etNama)
        val etKota = findViewById<EditText>(R.id.etKota)
        val btnKirim = findViewById<Button>(R.id.btnKirim)

        btnKirim.setOnClickListener {

            val intent = Intent(this, ResultActivity::class.java)

            intent.putExtra("nim", etNim.text.toString())
            intent.putExtra("nama", etNama.text.toString())
            intent.putExtra("kota", etKota.text.toString())

            startActivity(intent)
        }
    }
}