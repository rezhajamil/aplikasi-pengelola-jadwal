package com.example.pengingatjadwal.Fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecBerandaAdapter
import com.example.pengingatjadwal.Adapter.RecSemuaJadwalItem
import com.example.pengingatjadwal.Alarm.AlarmHelper
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
    lateinit var llKosong: LinearLayout

    //Variabel
    lateinit var recAdapter: RecBerandaAdapter
    lateinit var dbHelper: DbHelper
    lateinit var listJadwalSekarang: MutableList<JadwalModel>
    lateinit var listJadwalSekarangBeranda: MutableList<JadwalModel>
    lateinit var formKalender: Calendar
    lateinit var alarmHelper: AlarmHelper
    var calendar: Calendar = Calendar.getInstance()
    var isAllFieldsChecked: Boolean = false
    val formatNamaHari = SimpleDateFormat("EEEE")
    val formatTanggal = SimpleDateFormat("dd MMMM yyyy")
    val dateFormat= SimpleDateFormat("dd-MM-yyyy")
    val hariIni = Date()


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
        dbHelper = DbHelper(requireContext())
        recBeranda = rootView.findViewById(R.id.rec_beranda)
        tvHari = rootView.findViewById(R.id.tv_hari_beranda)
        tvTanggal = rootView.findViewById(R.id.tv_tanggal_beranda)
        llKosong = rootView.findViewById(R.id.ll_jadwal_kosong_beranda)

        alarmHelper = AlarmHelper(requireActivity())
        formKalender = Calendar.getInstance()
        formKalender.timeInMillis = System.currentTimeMillis()

        recBeranda.layoutManager = LinearLayoutManager(requireContext())

        setDayName()
        tvTanggal.text = "${formatTanggal.format(hariIni)}"
    }

    //Fungsi Ngeset Data ke Adapter
    fun setRecData() {
        if (listJadwalSekarangBeranda.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }
        recAdapter = RecBerandaAdapter(listJadwalSekarangBeranda,this, 1)
        recBeranda.adapter = recAdapter
    }

    //Fungsi Mengambil Data Jadwal Hari Ini dari DB
    fun getTodaySchedule() {
        listJadwalSekarang = dbHelper.getAllTodaySchedule()

        listJadwalSekarangBeranda = dbHelper.getAllTodayScheduleBeranda()

        for (jadwalModel in listJadwalSekarang) {
            onDelete(jadwalModel.id)
        }

    }

    //Fungsi Ubah Nama Hari
    fun setDayName() : String {
        var hari: String = ""

        if (formatNamaHari.format(hariIni).equals("Monday")) { tvHari.text = "Senin" }
        if (formatNamaHari.format(hariIni).equals("Tuesday")) { tvHari.text = "Selasa" }
        if (formatNamaHari.format(hariIni).equals("Wednesday")) { tvHari.text = "Rabu" }
        if (formatNamaHari.format(hariIni).equals("Thursday")) { tvHari.text = "Kamis" }
        if (formatNamaHari.format(hariIni).equals("Friday")) { tvHari.text = "Jumat" }
        return hari
    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: Int) {
        dbHelper.deleteSchedule(id)
    }

    //Fungsi Perbarui Data (sumber: Interface)
    override fun onUpdate(jadwalModel: JadwalModel) {
        val btnDoneScheduleView = LayoutInflater.from(activity)
            .inflate(R.layout.view_btm_sheet_selesaikan_jadwal, null, false)

        val edtCatatan = btnDoneScheduleView.findViewById<EditText>(R.id.edt_catatan_view_btm_selesai_mengajar)
        val mbtSelesai = btnDoneScheduleView.findViewById<MaterialButton>(R.id.mbt_selesai_view_btm_selesai_mengajar)

        val btmSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

        val myDate: Date = dateFormat.parse(jadwalModel.tanggal)
        calendar.time = myDate
        calendar.add(Calendar.DAY_OF_YEAR, +7)

        val newDate: Date = calendar.time

        btmSheetDialog.setContentView(btnDoneScheduleView)
        btmSheetDialog.show()

        mbtSelesai.setOnClickListener {
            if(edtCatatan.length() == 0) {
                edtCatatan.error = "Wajib Diisi"
            } else {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder
                    .setIcon(R.drawable.ic_warning_biru)
                    .setTitle("Perhatian")
                    .setMessage("Jadwalkan Kembali Untuk Minggu Depan?")
                    .setPositiveButton("Iya") {dialog, which ->
                        saveDataToDb(
                            jadwalModel.mapel,
                            jadwalModel.kelas,
                            jadwalModel.hari,
                            dateFormat.format(newDate),
                            jadwalModel.waktu
                        )
                        saveDataToDbBeranda(
                            jadwalModel.mapel,
                            jadwalModel.kelas,
                            jadwalModel.hari,
                            dateFormat.format(newDate),
                            jadwalModel.waktu
                        )
                        dbHelper.updateScheduleBeranda(jadwalModel.id, jadwalModel.mapel, jadwalModel.kelas,jadwalModel.hari, jadwalModel.tanggal, jadwalModel.waktu,"2", edtCatatan.text.toString())
                        btmSheetDialog.dismiss()
                        getTodaySchedule()
                        setRecData()
                    }
                    .setNegativeButton("Tidak") { dialog, which ->
                        dbHelper.updateScheduleBeranda(jadwalModel.id, jadwalModel.mapel, jadwalModel.kelas,jadwalModel.hari, jadwalModel.tanggal, jadwalModel.waktu,"2", edtCatatan.text.toString())
                        btmSheetDialog.dismiss()
                        getTodaySchedule()
                        setRecData()
                    }
                val dialog = dialogBuilder.create()
                dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
                dialog.show()
            }
        }
        getTodaySchedule()
        setRecData()
    }

    //Fungsi Simpan Data ke DB tbBeranda
    fun saveDataToDbBeranda(mapel: String, kelas: String, hari: String, tanggal: String, waktu: String) {
        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("hari", hari)
        values.put("tanggal", tanggal)
        values.put("waktu", waktu)
        values.put("status", "0")
        values.put("catatan", "0")

        dbHelper.saveNewScheduleBeranda(values)

        val waktu = waktu.split(":")

        alarmHelper.setAlarm(0, formKalender)
    }

    //Fungsi Simpan Data ke DB tbJadwal
    fun saveDataToDb(mapel: String, kelas: String, hari: String, tanggal: String, waktu: String) {
        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("hari", hari)
        values.put("tanggal", tanggal)
        values.put("waktu", waktu)
        values.put("status", "0")
        values.put("catatan", "0")

        dbHelper.saveNewSchedule(values)

        val waktu = waktu.split(":")

        alarmHelper.setAlarm(0, formKalender)
    }
}