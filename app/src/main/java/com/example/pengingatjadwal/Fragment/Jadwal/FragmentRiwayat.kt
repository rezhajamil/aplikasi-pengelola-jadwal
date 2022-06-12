package com.example.pengingatjadwal.Fragment.Jadwal

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecRiwayatAdapter
import com.example.pengingatjadwal.Adapter.RecSemuaJadwalItem
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog

class FragmentRiwayat: Fragment(), RecSemuaJadwalItem {

    //Variabel View
    lateinit var rootView: View
    lateinit var recRiwayat: RecyclerView
    lateinit var llKosong: LinearLayout
    lateinit var srcViewRiwayat: androidx.appcompat.widget.SearchView

    //Variabel
    lateinit var recAdapter: RecRiwayatAdapter
    lateinit var listRiwayatJadwal: MutableList<JadwalModel>
    lateinit var dbHelper: DbHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_riwayat, null, false)

        initView()

        getHistorySchedule()
        setRecData()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {
        dbHelper = DbHelper(requireContext())

        recRiwayat= rootView.findViewById(R.id.rec_riwayat)
        llKosong = rootView.findViewById(R.id.ll_jadwal_kosong_riwayat)
        srcViewRiwayat = rootView.findViewById(R.id.src_view_jadwal_riwayat)

        listRiwayatJadwal = dbHelper.getAllHistorySchedule()

        //Fungsi Pada Search View
        srcViewRiwayat.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchHistorySchedule(newText!!)
                setRecData()
                return false
            }

        })
    }

    //Fungsi Mencari Riwayat Jadwal
    fun searchHistorySchedule(kelas: String) {
        listRiwayatJadwal = dbHelper.searchScheduleBeranda(kelas)
    }

    //Fungsi Mendapatkan Riwayat Jadwal
    fun getHistorySchedule() {
        listRiwayatJadwal = dbHelper.getAllHistoryScheduleBeranda()
    }

    //Fungsi Mengeset Data ke Adapter
    fun setRecData() {
        if (listRiwayatJadwal.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }

        recAdapter = RecRiwayatAdapter(listRiwayatJadwal, this, 3)
        recRiwayat.layoutManager = LinearLayoutManager(requireContext())
        recRiwayat.adapter = recAdapter
    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: String) {
        TODO("Not yet implemented")
    }

    //Fungsi Perbauri Data (sumber: Interface)
    override fun onUpdate(jadwalModel: JadwalModel) {
        val btmSheetCatatanView = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_btm_sheet_catatan, null, false)

        val edtNotes = btmSheetCatatanView.findViewById<TextView>(R.id.edt_catatan_view_btm_sheet_catatan)
        val mbtClose = btmSheetCatatanView.findViewById<TextView>(R.id.mbt_tutup_view_btm_sheet_catatan)

        edtNotes.inputType = InputType.TYPE_NULL

        edtNotes.text = jadwalModel.catatan

        val btmSheetCatatan = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        btmSheetCatatan.setContentView(btmSheetCatatanView)
        btmSheetCatatan.show()

        mbtClose.setOnClickListener {
            btmSheetCatatan.dismiss()
        }
    }

}