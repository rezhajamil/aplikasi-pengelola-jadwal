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

    //Baca Semua Data Jadwal di DB
    fun getallSchedule(): MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("select * from tbJadwal", null)

        cursor.moveToFirst()

        if(cursor.count > 0) {
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

    //Baca Simpan Data Jadwal Baru di DB
    fun saveNewSchedule(values: ContentValues) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.insert("tbJadwal", null, values)
    }

    //Fungsi Perbarui Data di DB
    fun updateSchedule(id: Int, mapel: String, kelas:String, waktu: String) {
        dbConfig = DbConfig(context)

        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("waktu", waktu)
        values.put("status", 0)
        values.put("catatan", 0)

        db = dbConfig.writableDatabase

        db.update("tbJadwal", values, "id = ?", arrayOf(id.toString()))
    }

    //Fungsi Hapus Data dari DB
    fun deleteSchedule(id: Int) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.delete("tbJadwal", "id = ?", arrayOf(id.toString()))
    }
}