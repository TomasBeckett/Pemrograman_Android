package com.example.m4

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FormMahasiswa : AppCompatActivity() {

    lateinit var db: DatabaseHelper
    lateinit var imgFoto: ImageView
    var fotoPath: String = ""
    val IMAGE_PICK = 100
    private val TAG = "DATABASE_SQLITE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_mahasiswa)

        val etNIm = findViewById<EditText>(R.id.etNim)
        val etNama = findViewById<EditText>(R.id.etNama)
        val spProdi = findViewById<Spinner>(R.id.spProdi)
        val btnFoto = findViewById<Button>(R.id.btnFoto)
        val btnSimpan = findViewById<Button>(R.id.btnSimpan)

        imgFoto = findViewById(R.id.imgFoto)

        db = DatabaseHelper(this)

        val prodi = arrayOf(
            "Informatika",
            "Sistem Informasi",
            "Manajemen"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, prodi)

        spProdi.adapter = adapter

        btnFoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK)
        }

        btnSimpan.setOnClickListener {
            val nim = etNIm.text.toString()
            val nama = etNama.text.toString()
            val prodiValue = spProdi.selectedItem.toString()

            db.insertData(nim, nama, prodiValue, fotoPath)
//            val dataDB: String = db.getAllData().toString()
//            Log.d(TAG, "Isi Database SQlite")
//            Log.d(TAG, dataDB)

            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data

            imgFoto.setImageURI(uri)

            fotoPath = uri.toString()
        }
    }
}
