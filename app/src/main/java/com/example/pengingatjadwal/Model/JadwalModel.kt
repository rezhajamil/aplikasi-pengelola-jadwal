package com.example.pengingatjadwal.Model

data class JadwalModel(
    var id: String = "",
    var kegiatan: String = "",
    var tim: String = "",
    var hari: String = "",
    var tanggal: String = "",
    var waktu: String = "",
    var status: Int = 0,
    var catatan: String = ""
)
