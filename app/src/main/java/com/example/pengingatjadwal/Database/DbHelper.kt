package com.example.pengingatjadwal.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.example.pengingatjadwal.Model.JadwalModel
import java.text.SimpleDateFormat
import java.util.*

class DbHelper(val context: Context) {

    //Variabel
    lateinit var db: SQLiteDatabase
    lateinit var dbConfig: DbConfig
    lateinit var cursor: Cursor

    //Baca Semua Data Jadwal di DB
    fun getAllSchedule(): MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE status = 0 ORDER BY tanggal ASC", null)

        cursor.moveToFirst()

        if(cursor.count > 0) {
            do {
                jadwal.add(
                    JadwalModel(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6).toInt(),
                        cursor.getString(7)
                    )
                )
            } while (cursor.moveToNext())
        }

        return jadwal
    }

    //Baca Semua Data Jadwal di DB Berdasarkan Hari
    fun getAllScheduleByDay(hari: String) : MutableList<JadwalModel> {

        val schedules = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db =dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE hari = '$hari' ORDER BY tanggal ASC", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                schedules.add(
                    JadwalModel(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6).toInt(),
                        cursor.getString(7)
                    )
                )
            } while (cursor.moveToNext())
        }

        return schedules
    }
    //Simpan Data Jadwal Baru di DB
    fun saveNewSchedule(values: ContentValues) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.insert("tbJadwal", null, values)
    }

    //Baca Semua Data di DB yang Hari Ini
    fun getAllTodaySchedule() : MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        val hariIni = Date()
        val formatHari = SimpleDateFormat("d-MM-yyyy")

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE tanggal LIKE '${formatHari.format(hariIni)}' AND status != 2", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6).toInt(),
                        cursor.getString(7)
                    )
                )
            } while (cursor.moveToNext())
        }
        return jadwal
    }

    //Baca Semua Data di DB yang Riwayat
    fun getAllHistorySchedule() : MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        val hariIni = Date()
        val formatHari = SimpleDateFormat("d-MM-yyyy")

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE status = '2'", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0).toInt(),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6).toInt(),
                        cursor.getString(7)
                    )
                )
            } while (cursor.moveToNext())
        }
        return jadwal
    }

    //Fungsi Perbarui Data di DB
    fun updateSchedule(id: Int, mapel: String, kelas: String, hari: String, tanggal: String,  waktu: String, status: String, catatan: String) {
        dbConfig = DbConfig(context)

        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("hari", hari)
        values.put("tanggal", tanggal)
        values.put("waktu", waktu)
        values.put("status", status)
        values.put("catatan", catatan)

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