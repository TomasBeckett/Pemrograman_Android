package com.example.m4

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "kampus.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = """
            CREATE TABLE mahasiswa (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nim INTEGER,
            nama TEXT,
            prodi TEXT,
            foto TEXT
            )
        """

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS mahasiswa")
        onCreate(db)
    }

    fun insertData(nim: String, nama: String, prodi: String, foto: String) {
        val db = writableDatabase

        val query = """
            INSERT INTO mahasiswa (nim, nama, prodi, foto)
            VALUES ('$nim', '$nama', '$prodi', '$foto')
        """

        db.execSQL(query)
    }

//    fun getAllData(): String {
//        val db = readableDatabase
//        val cursor = db.rawQuery("SELECT * FROM mahasiswa", null)
//
//        var data = ""
//
//        while (cursor.moveToNext()) {
//            val id = cursor.getInt(0)
//            val nim = cursor.getString(1)
//            val nama = cursor.getString(2)
//            val prodi = cursor.getString(3)
//            val foto = cursor.getString(4)
//
//            data += "ID:$id NIM:$nim Nama:$nama Prodi:$prodi Foto:$foto\n"
//        }
//        cursor.close()
//        return data
//    }

    fun getAllData(): List<Mahasiswa> {
        val list = mutableListOf<Mahasiswa>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM mahasiswa", null)

        if (cursor.moveToFirst()) {
            do {
                val mhs = Mahasiswa(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
                list.add(mhs)
                } while (cursor.moveToNext())
            }
        cursor.close()
        return list
        }
    }