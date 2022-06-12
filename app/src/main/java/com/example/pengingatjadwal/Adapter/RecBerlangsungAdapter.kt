package com.example.pengingatjadwal.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class RecBerlangsungAdapter(val listJadwal: MutableList<JadwalModel>,private val listener: (JadwalModel) -> Unit): RecyclerView.Adapter<RecBerlangsungAdapter.ViewHolder>(){

    lateinit var contextAdapter: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        contextAdapter=parent.context
        val inflatedView=layoutInflater.inflate(R.layout.row_jadwal,null,false)
        return ViewHolder(inflatedView)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToView(listJadwal[position],listener)
    }

    override fun getItemCount(): Int = listJadwal.size

    class ViewHolder(val v : View): RecyclerView.ViewHolder(v) {

        val tvKegiatan = v.findViewById<TextView>(R.id.tv_kegiatan_berlangsung)
        val tvJam = v.findViewById<TextView>(R.id.tv_jam_berlangsung)
        val tvTanggal = v.findViewById<TextView>(R.id.tv_tanggal_berlangsung)
        val tvTim = v.findViewById<TextView>(R.id.tv_tim_berlangsung)
        val mbtUbah = v.findViewById<MaterialButton>(R.id.mbt_ubah_berlangsung)

        //Fungsi atur data dari Model
        fun setDataToView(jadwalModel: JadwalModel, listener: (JadwalModel) -> Unit) {
            tvKegiatan.text = jadwalModel.kegiatan
            tvJam.text = jadwalModel.waktu
            tvTanggal.text = jadwalModel.tanggal
            tvTim.text = jadwalModel.tim

            mbtUbah.setOnClickListener {
                val alertDialogUbah = AlertDialog.Builder(v.context)

                alertDialogUbah
                    .setTitle("Ubah Jadwal")
                    .setIcon(R.drawable.ic_ubah_biru)
                    .setMessage("Hapus atau Perbarui?")
                    .setPositiveButton("Ubah") { dialog, which ->
//                        recSemuaJadwalItem.onUpdate(jadwalModel)
                            listener(jadwalModel)
                    }
                    .setNegativeButton("Batal") { dialog, which ->
                    }
                    .setNeutralButton("Hapus") { dialog, which ->
                        val alertDialogHapus = AlertDialog.Builder(v.context)
                        alertDialogHapus
                            .setTitle("Perhatian")
                            .setIcon(R.drawable.ic_warning_biru)
                            .setMessage("Yakin Hapus Jadwal Ini?")
                            .setPositiveButton("Hapus") {dialog, which ->
//                                recSemuaJadwalItem.onDelete(jadwalModel.id)
                                FirebaseDatabase.getInstance().getReference("Jadwal").child(jadwalModel.id).removeValue()
                            }
                            .setNegativeButton("Batal") { dialog, which ->
                            }

                        val dialogHapus = alertDialogHapus.create()
                        dialogHapus.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
                        dialogHapus.show()
                    }

                val dialogUbah = alertDialogUbah.create()
                dialogUbah.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
                dialogUbah.show()

            }
        }
    }

}