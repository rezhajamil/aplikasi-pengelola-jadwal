package com.example.pengingatjadwal.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
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
                        cursor.getString(0),
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

    //Cari Data Jadwal di DB
    fun searchSchedule(kelas: String): MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE status = 2 AND kelas LIKE '%' || '${kelas}' || '%' ", null)

        cursor.moveToFirst()

        if(cursor.count > 0) {
            do {
                jadwal.add(
                    JadwalModel(
                        cursor.getString(0),
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

    //Cari Data Jadwal di DB tbBeranda
    fun searchScheduleBeranda(kelas: String): MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbBeranda WHERE status = 2 AND kelas LIKE '%' || '${kelas}' || '%' ", null)

        cursor.moveToFirst()

        if(cursor.count > 0) {
            do {
                jadwal.add(
                    JadwalModel(
                        cursor.getString(0),
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

    //Baca Semua Data Jadwal di DB tbJadwal Berdasarkan Hari
    fun getAllScheduleByDay(hari: String) : MutableList<JadwalModel> {

        val schedules = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db =dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE hari = '$hari' AND status = 0 ORDER BY tanggal ASC", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                schedules.add(
                    JadwalModel(
                        cursor.getString(0),
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

    //Simpan Data Jadwal Baru di DB tbJadwal
    fun saveNewSchedule(values: ContentValues) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.insert("tbJadwal", null, values)
    }

    //Simpan Data Jadwal Baru di DB tbBeranda
    fun saveNewScheduleBeranda(values: ContentValues) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.insert("tbBeranda", null, values)
    }

    //Baca Semua Data di DB tbJadwal yang Hari Ini
    fun getAllTodaySchedule() : MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        val hariIni = Date()
        val formatHari = SimpleDateFormat("d-MM-yyyy")

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE tanggal LIKE '${formatHari.format(hariIni)}' AND status != 2 ORDER BY waktu ASC", null)

//        Log.v("jadwals",formatHari.format(hariIni))
//        Log.v("jadwals", cursor.toString())
//        Log.v("jadwals", "SELECT * FROM tbJadwal WHERE tanggal LIKE '${formatHari.format(hariIni)}' AND status != 2 ORDER BY waktu ASC")
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0),
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

    //Baca Semua Data di DB tbBeranda yang Hari Ini
    fun getAllTodayScheduleBeranda() : MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        val hariIni = Date()
        val formatHari = SimpleDateFormat("d-MM-yyyy")

        cursor = db.rawQuery("SELECT * FROM tbBeranda WHERE tanggal LIKE '${formatHari.format(hariIni)}' AND status != 2 ORDER BY waktu DESC", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0),
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

    //Baca Semua Waktu di DB Sesuai Tanggal
    fun getTimeByDate(tanggal: String) : MutableList<JadwalModel>{
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE tanggal LIKE '$tanggal' AND status != 2", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0),
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

        cursor = db.rawQuery("SELECT * FROM tbJadwal WHERE status = '2'", null)

        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0),
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

    //Baca Semua Data di DB tBeranda yang Riwayat
    fun getAllHistoryScheduleBeranda() : MutableList<JadwalModel> {
        val jadwal = mutableListOf<JadwalModel>()

        dbConfig = DbConfig(context)

        db = dbConfig.readableDatabase

        cursor = db.rawQuery("SELECT * FROM tbBeranda WHERE status = '2'", null)

        if (cursor.count > 0) {
            cursor.moveToLast()
            do {
                jadwal.add (
                    JadwalModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6).toInt(),
                        cursor.getString(7)
                    )
                )
            } while (cursor.moveToPrevious())
        }
        return jadwal
    }

    //Fungsi Perbarui Data di DB tbJadwal
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

    //Fungsi Perbarui Data di DB tbBeranda
    fun updateScheduleBeranda(id: String, mapel: String, kelas: String, hari: String, tanggal: String, waktu: String, status: String, catatan: String) {
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

        db.update("tbBeranda", values, "id = ?", arrayOf(id.toString()))
    }

    //Fungsi Hapus Data dari DB tbJadwal
    fun deleteSchedule(id: String) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        db.delete("tbJadwal", "id = ?", arrayOf(id.toString()))
    }

    //Fungsi Hapus Data dari DB yang Lewat
    fun deleteSchedulePast(tanggal: String) {
        dbConfig = DbConfig(context)

        db = dbConfig.writableDatabase

        /*db.rawQuery("DELETE FROM tbJadwal WHERE tanggal LIKE $tanggal", null)*/
        db.delete("tbJadwal", "tanggal = ?", arrayOf(tanggal))
    }
}