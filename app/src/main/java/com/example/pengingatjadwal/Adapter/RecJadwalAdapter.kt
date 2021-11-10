package com.example.pengingatjadwal.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R

class RecJadwalAdapter(val listJadwal: MutableList<JadwalModel>): RecyclerView.Adapter<RecJadwalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.row_jadwal, null, false)
    )


    override fun onBindViewHolder(holder: RecJadwalAdapter.ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position])
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v : View): RecyclerView.ViewHolder(v) {

        val tvMapel = v.findViewById<TextView>(R.id.tv_mapel)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam)
        val tvKelas= v.findViewById<TextView>(R.id.tv_kelas)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel) {
            tvMapel.text = jadwalModel.mapel
            tvJam.text = jadwalModel.jam
            tvKelas.text = jadwalModel.kelas
        }
    }
}