package com.example.pengingatjadwal.Jadwal

import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecJadwalAdapter
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentBerlangsung: Fragment() {

    //Variabel View
    lateinit var rootView: View
    lateinit var fabTambah: FloatingActionButton
    lateinit var recBerlangsung: RecyclerView

    //Variabel
    lateinit var dbHelper: DbHelper
    lateinit var recAdapter: RecJadwalAdapter
    lateinit var listJadwal: MutableList<JadwalModel>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_berlangsung, null, false)

        initView()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {
        dbHelper = DbHelper(rootView.context)
        listJadwal = dbHelper.getSemuaJadwal()

        recAdapter = RecJadwalAdapter(listJadwal)

        recBerlangsung = rootView.findViewById(R.id.rec_berlangsung)
        recBerlangsung.layoutManager = LinearLayoutManager(context)
        recBerlangsung.adapter = recAdapter

        fabTambah = rootView.findViewById(R.id.fab_tambah_jadwal)
        fabTambah.setOnClickListener(View.OnClickListener {
            val values = ContentValues()

            values.put("mapel", "MTK")
            values.put("kelas", "1 A")
            values.put("time", "3232")
            values.put("status", "0")
            values.put("catatan", "0")

            dbHelper.simpanJadwalBaru(values)
        })
    }
}