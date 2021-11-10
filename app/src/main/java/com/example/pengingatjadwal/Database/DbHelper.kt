package com.example.pengingatjadwal.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import com.example.pengingatjadwal.Model.JadwalModel

class DbHelper(val context: Context) {

    //Variabel
    lateinit var db: SQLiteDatabase
    lateinit var dbConfig: DbConfig
    lateinit var cursor: Cursor

    fun getSemuaJadwal(): MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("select * from tbJadwal", null)

        if(cursor!=null && cursor.moveToFirst()) {
            do {
                jadwal.add(
                    JadwalModel(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4).toInt(),
                        cursor.getString(5)
                    )
                )
            } while (cursor.moveToNext())
        }

        return jadwal
    }

    fun simpanJadwalBaru(values: ContentValues) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.insert("tbJadwal", null, values)
    }
}