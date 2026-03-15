package com.example.activityandintet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvNim = findViewById<TextView>(R.id.tvNim)
        val tvNama = findViewById<TextView>(R.id.tvNama)
        val tvKota = findViewById<TextView>(R.id.tvKota)
        val btnKembali = findViewById<Button>(R.id.btnKembali)

        val nim = intent.getStringExtra("nim")
        val nama = intent.getStringExtra("nama")
        val kota = intent.getStringExtra("kota")

        tvNim.text = "NIM : $nim"
        tvNama.text = "Nama : $nama"
        tvKota.text = "Kota : $kota"

        btnKembali.setOnClickListener {
            finish()
        }
    }
}