package com.example.pengingatjadwal.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton

class RecBerandaAdapter(val listJadwal: MutableList<JadwalModel>,private val listener: (JadwalModel) -> Unit): RecyclerView.Adapter<RecBerandaAdapter.ViewHolder>(){


    lateinit var contextAdapter: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecBerandaAdapter.ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        contextAdapter=parent.context
        val inflatedView=layoutInflater.inflate(R.layout.row_beranda,null,false)
        return RecBerandaAdapter.ViewHolder(inflatedView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position],listener,contextAdapter)
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {

        val tvKegiatan = v.findViewById<TextView>(R.id.tv_kegiatan_beranda)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam_beranda)
        val tvTim = v.findViewById<TextView>(R.id.tv_tim_beranda)
        val mbtSelesai = v.findViewById<MaterialButton>(R.id.mbt_selesai_beranda)

        //Fungsi atur data dari Model
        fun setDataToView(
            jadwalModel: JadwalModel,
            listener: (JadwalModel) -> Unit,
            contextAdapter: Context
        ) {
            tvKegiatan.text = jadwalModel.kegiatan
            tvJam.text = jadwalModel.waktu
            tvTim.text = jadwalModel.tim

            if (jadwalModel.status==1){
                mbtSelesai.visibility=View.GONE
            }

            mbtSelesai.setOnClickListener {
                listener(jadwalModel)
            }
        }



    }

}