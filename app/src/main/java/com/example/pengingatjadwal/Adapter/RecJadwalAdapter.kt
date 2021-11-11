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

class RecJadwalAdapter(val listJadwal: MutableList<JadwalModel>, val recSemuaJadwalItem: RecSemuaJadwalItem): RecyclerView.Adapter<RecJadwalAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.row_jadwal, null, false),
        recSemuaJadwalItem
    )


    override fun onBindViewHolder(holder: RecJadwalAdapter.ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position])
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v : View, val recSemuaJadwalItem: RecSemuaJadwalItem): RecyclerView.ViewHolder(v) {

        val tvMapel = v.findViewById<TextView>(R.id.tv_mapel)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam)
        val tvKelas = v.findViewById<TextView>(R.id.tv_kelas)
        val mbtUbah = v.findViewById<MaterialButton>(R.id.mbt_ubah)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel) {
            tvMapel.text = jadwalModel.mapel
            tvJam.text = jadwalModel.waktu
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
                        Toast.makeText(v.context, "Dibatalkan", Toast.LENGTH_SHORT).show()
                    }
                    .setNeutralButton("Hapus") { dialog, which ->
                        recSemuaJadwalItem.onDelete(jadwalModel.id)
                    }
                    .show()
            }
        }
    }

}