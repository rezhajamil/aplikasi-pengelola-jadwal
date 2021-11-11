package com.example.pengingatjadwal.Adapter

import com.example.pengingatjadwal.Model.JadwalModel

interface RecSemuaJadwalItem {

    fun onDelete(id: Int)

    fun onUpdate(jadwalModel: JadwalModel)

}