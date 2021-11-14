package com.example.pengingatjadwal.Jadwal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecJadwalAdapter
import com.example.pengingatjadwal.Adapter.RecSemuaJadwalItem
import com.example.pengingatjadwal.Alarm.AlarmHelper
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class FragmentBerlangsung: Fragment(), RecSemuaJadwalItem {
    //Variabel View
    lateinit var rootView: View
    lateinit var fabTambah: FloatingActionButton
    lateinit var recBerlangsung: RecyclerView
    lateinit var edtKelas: EditText
    lateinit var edtTanggal: EditText
    lateinit var edtWaktu: EditText

    //Variabel
    lateinit var dbHelper: DbHelper
    lateinit var recAdapter: RecJadwalAdapter
    lateinit var listJadwal: MutableList<JadwalModel>
    lateinit var formKalender: Calendar
    lateinit var alarmHelper: AlarmHelper


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

        alarmHelper = AlarmHelper(requireActivity())
        alarmHelper.createNotificationChannel(requireActivity())
        formKalender = Calendar.getInstance()
        formKalender.timeInMillis = System.currentTimeMillis()

        recBerlangsung = rootView.findViewById(R.id.rec_berlangsung)
        recBerlangsung.layoutManager = LinearLayoutManager(context)

        setRecData()

        fabTambah = rootView.findViewById(R.id.fab_tambah_jadwal)
        fabTambah.setOnClickListener {
            addDataDialog(JadwalModel())

            setRecData()
        }
    }

    //Fungsi Ngeset Data ke Adapter
    fun setRecData() {
        listJadwal = dbHelper.getAllSchedule()
        recAdapter = RecJadwalAdapter(listJadwal, this, 2)
        recBerlangsung.adapter = recAdapter
    }

    //Fungsi Membuat BottomSheet Dialog untuk Menambahkan Data
    fun addDataDialog(jadwalModel: JadwalModel) {
        val btmSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_btm_sheet_tambah_jadwal, null, false)
        val btmSheetDialog = BottomSheetDialog(requireContext())

        val edtMapel = btmSheetView.findViewById<EditText>(R.id.edt_mapel_tambah_jadwal)
        edtKelas = btmSheetView.findViewById(R.id.edt_kelas_tambah_jadwal)
        edtTanggal = btmSheetView.findViewById(R.id.edt_tanggal_tambah_jadwal)
        edtWaktu = btmSheetView.findViewById(R.id.edt_waktu_tambah_jadwal)
        val mbtTambah = btmSheetView.findViewById<MaterialButton>(R.id.btn_tambah_tambah_jadwal)

        btmSheetDialog.setContentView(btmSheetView)

        edtKelas.isClickable = true
        edtKelas.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectClass()
            }
        }

        edtTanggal.isClickable = true
        edtTanggal.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectDate()
            }
        }

        edtWaktu.isClickable = true
        edtWaktu.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectTime()
            }
        }

        if (jadwalModel.mapel != "") {
            edtMapel.setText(jadwalModel.mapel)
            edtKelas.setText(jadwalModel.kelas)
            edtTanggal.setText(jadwalModel.tanggal)
            edtWaktu.setText(jadwalModel.waktu)


            val jadwalBaru = JadwalModel(
                jadwalModel.id,
                edtMapel.text.toString(),
                edtKelas.text.toString(),
                edtTanggal.text.toString(),
                edtWaktu.text.toString()
            )

            mbtTambah.setText("Perbarui")
            mbtTambah.setOnClickListener {
                dbHelper.updateSchedule(
                    jadwalBaru.id,
                    edtMapel.text.toString(),
                    edtKelas.text.toString(),
                    edtTanggal.text.toString(),
                    edtWaktu.text.toString(),
                    "0",
                    "0")
                setRecData()
                alarmHelper.setAlarm(0, formKalender)
                btmSheetDialog.dismiss()
            }

        } else {
            mbtTambah.setOnClickListener {
                saveDataToDb(
                    edtMapel.text.toString(),
                    edtKelas.text.toString(),
                    edtTanggal.text.toString(),
                    edtWaktu.text.toString()
                )
                setRecData()
                btmSheetDialog.dismiss()
            }
        }

        btmSheetDialog.show()
    }

    //Fungsi Simpan Data ke DB
    fun saveDataToDb(mapel: String, kelas: String, tanggal: String, waktu: String) {
        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("tanggal", tanggal)
        values.put("waktu", waktu)
        values.put("status", "0")
        values.put("catatan", "0")

        dbHelper.saveNewSchedule(values)

        val waktu = waktu.split(":")

        alarmHelper.setAlarm(0, formKalender)

        setRecData()
    }

    //Fungsi Pilih Kelas
    fun selectClass() {
        val kelas = arrayOf("Kelas 1-A", "Kelas 1-B")
        val dialog = AlertDialog.Builder(requireContext())
        dialog
            .setTitle("Pilih Kelas")
            .setItems(kelas) { dialog, position ->
                Toast.makeText(requireContext(), kelas[position], Toast.LENGTH_SHORT).show()
                edtKelas.setText(kelas[position].toString())
            }
            .show()
    }

    //Fungsi Pilih Hari
    fun selectDate() {
        val kalender = Calendar.getInstance()

        val datePicker = DatePickerDialog(requireContext(), {view, year, month, dayOfMonth ->
            edtTanggal.setText("$dayOfMonth-${month+1}-$year")
            formKalender[Calendar.DAY_OF_MONTH] = dayOfMonth
            formKalender[Calendar.MONTH] = month
            formKalender[Calendar.YEAR] = year
        },  kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DATE))

        datePicker.show()
    }

    //Fungsi Pilih Waktu
    fun selectTime() {
        val kalender = Calendar.getInstance()
        val timePicker = TimePickerDialog(requireContext(), { timePicker, hour, minute ->
            edtWaktu.setText("$hour:$minute")
            formKalender[Calendar.HOUR_OF_DAY] = hour
            formKalender[Calendar.MINUTE] = minute
            formKalender[Calendar.SECOND] = 0
            formKalender[Calendar.MILLISECOND] = 0
        }, kalender.get(Calendar.HOUR), kalender.get(Calendar.MINUTE), true)
        timePicker.show()
    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: Int) {
        dbHelper.deleteSchedule(id)
        setRecData()
    }

    override fun onUpdate(jadwalModel: JadwalModel) {
        addDataDialog(jadwalModel)
    }

}