package com.example.pengingatjadwal.Model

data class JadwalModel(
    val id: Int,
    val mapel: String,
    val kelas: String,
    val jam: String,
    val status: Int,
    val catatan: String) {
}