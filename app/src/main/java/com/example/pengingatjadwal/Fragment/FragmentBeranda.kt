package com.example.pengingatjadwal.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecBerandaAdapter
import com.example.pengingatjadwal.Adapter.RecJadwalAdapter
import com.example.pengingatjadwal.Adapter.RecSemuaJadwalItem
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class FragmentBeranda: Fragment(), RecSemuaJadwalItem {

    //Variabel View
    lateinit var rootView: View
    lateinit var tvHari: TextView
    lateinit var tvTanggal: TextView
    lateinit var recBeranda: RecyclerView

    //Variabel
    lateinit var recAdapter: RecBerandaAdapter
    lateinit var dbHelper: DbHelper
    lateinit var listJadwalSekarang: MutableList<JadwalModel>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_beranda, null, false)

        initView()

        getTodaySchedule()
        setRecData()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {
        val hariIni = Date()
        val formatNamaHari = SimpleDateFormat("EEEE")
        val formatTanggal = SimpleDateFormat("dd MMMM yyyy")

        dbHelper = DbHelper(requireContext())
        recBeranda = rootView.findViewById(R.id.rec_beranda)
        tvHari = rootView.findViewById(R.id.tv_hari_beranda)
        tvTanggal = rootView.findViewById(R.id.tv_tanggal_beranda)

        recBeranda.layoutManager = LinearLayoutManager(requireContext())

        tvHari.text = "${formatNamaHari.format(hariIni)}"
        tvTanggal.text = "${formatTanggal.format(hariIni)}"
    }

    //Fungsi Ngeset Data ke Adapter
    fun setRecData() {
        recAdapter = RecBerandaAdapter(listJadwalSekarang,this, 1)
        recBeranda.adapter = recAdapter
    }

    //Fungsi Mengambil Data Jadwal Hari Ini dari DB
    fun getTodaySchedule() {
        listJadwalSekarang = dbHelper.getAllTodaySchedule()
    }

    override fun onDelete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun onUpdate(jadwalModel: JadwalModel) {
        val btnDoneScheduleView = LayoutInflater.from(activity)
            .inflate(R.layout.view_btm_sheet_selesaikan_jadwal, null, false)

        val edtCatatan = btnDoneScheduleView.findViewById<EditText>(R.id.edt_catatan_view_btm_selesai_mengajar)
        val mbtSelesai = btnDoneScheduleView.findViewById<MaterialButton>(R.id.mbt_selesai_view_btm_selesai_mengajar)

        val btmSheetDialog = BottomSheetDialog(requireContext())
        btmSheetDialog.setContentView(btnDoneScheduleView)
        btmSheetDialog.show()

        mbtSelesai.setOnClickListener {
            dbHelper.updateSchedule(jadwalModel.id, jadwalModel.mapel, jadwalModel.kelas, jadwalModel.tanggal, jadwalModel.waktu,"2", edtCatatan.text.toString())

            btmSheetDialog.dismiss()
            getTodaySchedule()
            setRecData()

        }

        getTodaySchedule()
        setRecData()
    }
}