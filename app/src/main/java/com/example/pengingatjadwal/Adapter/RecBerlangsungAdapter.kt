package com.example.pengingatjadwal.Adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RecBerlangsungAdapter(val listJadwal: MutableList<JadwalModel>, val recSemuaJadwalItem: RecSemuaJadwalItem, val code: Int): RecyclerView.Adapter<RecBerlangsungAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.row_jadwal, null, false),
        recSemuaJadwalItem
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position])
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v : View, val recSemuaJadwalItem: RecSemuaJadwalItem): RecyclerView.ViewHolder(v) {

        val tvMapel = v.findViewById<TextView>(R.id.tv_mapel_berlangsung)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam_berlangsung)
        val tvTanggal = v.findViewById<TextView>(R.id.tv_tanggal_berlangsung)
        val tvKelas = v.findViewById<TextView>(R.id.tv_kelas_berlangsung)
        val mbtUbah = v.findViewById<MaterialButton>(R.id.mbt_ubah_berlangsung)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel) {
            tvMapel.text = jadwalModel.mapel
            tvJam.text = jadwalModel.waktu
            tvTanggal.text = jadwalModel.tanggal
            tvKelas.text = jadwalModel.kelas

            mbtUbah.setOnClickListener {
                val dialog = AlertDialog.Builder(v.context)

                dialog
                    .setTitle("Ubah Jadwal")
                    .setMessage("Hapus atau Perbarui?")
                    .setPositiveButton("Ubah") { dialog, which ->
                        recSemuaJadwalItem.onUpdate(jadwalModel)
                    }
                    .setNegativeButton("Batal") { dialog, which ->
                    }
                    .setNeutralButton("Hapus") { dialog, which ->
                        val dialog = AlertDialog.Builder(v.context)
                        dialog
                            .setTitle("Perhatian")
                            .setMessage("Yakin Hapus Jadwal Ini?")
                            .setPositiveButton("Hapus") {dialog, which ->
                                recSemuaJadwalItem.onDelete(jadwalModel.id)
                            }
                            .setNegativeButton("Batal") { dialog, which ->
                            }
                    }
                    .show()
            }
        }
    }

}