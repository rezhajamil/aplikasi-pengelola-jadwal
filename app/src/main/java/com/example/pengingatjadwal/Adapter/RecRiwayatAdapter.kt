package com.example.pengingatjadwal.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton

class RecRiwayatAdapter(val listJadwal: MutableList<JadwalModel>, val recSemuaJadwalItem: RecSemuaJadwalItem, val code: Int): RecyclerView.Adapter<RecRiwayatAdapter.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.row_riwayat, null, false),
        recSemuaJadwalItem
    )

    //Untuk mencegah data berubah saat discroll
    override fun getItemViewType(position: Int): Int {
        return position
    }

    //Untuk mencegah data berubah saat discroll
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position])

        if (position > 0) {
            if (listJadwal.get(position).tanggal.equals(listJadwal.get(position-1).tanggal)) {
                holder.tvTanggal.isVisible = false
            }
        }
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(v : View, val recSemuaJadwalItem: RecSemuaJadwalItem): RecyclerView.ViewHolder(v) {

        val tvTanggal = v.findViewById<TextView>(R.id.tv_tanggal_riwayat)
        val tvKegiatan = v.findViewById<TextView>(R.id.tv_kegiatan_riwayat)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam_riwayat)
        val tvTim = v.findViewById<TextView>(R.id.tv_tim_riwayat)
        val mbtInfo = v.findViewById<MaterialButton>(R.id.mbt_info_riwayat)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel) {
            tvKegiatan.text = jadwalModel.kegiatan
            tvJam.text = jadwalModel.waktu
            tvTim.text = jadwalModel.tim
            tvTanggal.text = jadwalModel.tanggal

            mbtInfo.setOnClickListener {
                recSemuaJadwalItem.onUpdate(jadwalModel)
            }
        }
    }

}