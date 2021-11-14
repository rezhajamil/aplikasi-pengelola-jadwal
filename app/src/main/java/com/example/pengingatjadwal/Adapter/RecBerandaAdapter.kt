package com.example.pengingatjadwal.Adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton

class RecBerandaAdapter(val listJadwal: MutableList<JadwalModel>, val recSemuaJadwalItem: RecSemuaJadwalItem, val code: Int): RecyclerView.Adapter<RecBerandaAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.row_beranda, null, false),
        recSemuaJadwalItem
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position])
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v : View, val recSemuaJadwalItem: RecSemuaJadwalItem): RecyclerView.ViewHolder(v) {

        val tvMapel = v.findViewById<TextView>(R.id.tv_mapel_beranda)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam_beranda)
        val tvKelas = v.findViewById<TextView>(R.id.tv_kelas_beranda)
        val mbtSelesai = v.findViewById<MaterialButton>(R.id.mbt_selesai_beranda)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel) {
            tvMapel.text = jadwalModel.mapel
            tvJam.text = jadwalModel.waktu
            tvKelas.text = jadwalModel.kelas

            mbtSelesai.setOnClickListener {
                recSemuaJadwalItem.onUpdate(jadwalModel)
            }
        }
    }

}