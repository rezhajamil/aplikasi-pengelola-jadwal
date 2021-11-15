package com.example.pengingatjadwal.Model

data class JadwalModel(
    val id: Int = 0,
    val mapel: String = "",
    val kelas: String = "",
    val hari: String = "",
    val tanggal: String = "",
    val waktu: String = "",
    val status: Int = 0,
    val catatan: String = ""
)
