package com.example.pengingatjadwal.Database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.sql.SQLException

class DbConfig(context: Context): SQLiteOpenHelper(context, "dbJadwal", null, 1) {

    //Buat Tabel
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db!!.execSQL(
                "CREATE TABLE tbJadwal (id INTEGER PRIMARY KEY AUTOINCREMENT, mapel TEXT, kelas TEXT, tanggal TEXT, waktu TEXT, status INTEGER, catatan TEXT)"
            )
        } catch (e: SQLException) {
            Log.d("Kesalahan DB", "Error ${e.message}")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}