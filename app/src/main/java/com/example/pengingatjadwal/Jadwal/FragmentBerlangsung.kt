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
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentBerlangsung: Fragment(), RecSemuaJadwalItem {
    //Variabel View
    lateinit var rootView: View
    lateinit var fabTambah: FloatingActionButton
    lateinit var recBerlangsung: RecyclerView
    lateinit var edtKelas: EditText
    lateinit var edtWaktu: EditText

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
        listJadwal = dbHelper.getallSchedule()
        recAdapter = RecJadwalAdapter(listJadwal, this)
        recBerlangsung.adapter = recAdapter
    }

    //Fungsi Membuat BottomSheet Dialog untuk Menambahkan Data
    fun addDataDialog(jadwalModel: JadwalModel) {
        val btmSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_btm_sheet_tambah_jadwal, null, false)
        val btmSheetDialog = BottomSheetDialog(requireContext())

        val edtMapel = btmSheetView.findViewById<EditText>(R.id.edt_mapel_tambah_jadwal)
        edtKelas = btmSheetView.findViewById(R.id.edt_kelas_tambah_jadwal)
        edtWaktu = btmSheetView.findViewById(R.id.edt_waktu_tambah_jadwal)
        val mbtTambah = btmSheetView.findViewById<MaterialButton>(R.id.btn_tambah_tambah_jadwal)

        btmSheetDialog.setContentView(btmSheetView)

        edtKelas.isClickable = true
        edtKelas.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectClass()
            }
        }

        edtWaktu.isClickable = true
        edtWaktu.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                //TODO Cek Kalender
                selectTime()
            }
        }

        if (jadwalModel.mapel != "") {
            edtMapel.setText(jadwalModel.mapel)
            edtKelas.setText(jadwalModel.kelas)
            edtWaktu.setText(jadwalModel.waktu)


            val jadwalBaru = JadwalModel(
                jadwalModel.id,
                edtMapel.text.toString(),
                edtKelas.text.toString(),
                edtWaktu.text.toString()
            )

            mbtTambah.setText("Perbarui")
            mbtTambah.setOnClickListener {
                dbHelper.updateSchedule(
                    jadwalBaru.id,
                    edtMapel.text.toString(),
                    edtKelas.text.toString(),
                    edtWaktu.text.toString())
                setRecData()
                btmSheetDialog.dismiss()
            }

        } else {
            mbtTambah.setOnClickListener {
                saveDataToDb(
                    edtMapel.text.toString(),
                    edtKelas.text.toString(),
                    edtWaktu.text.toString()
                )
                setRecData()
                btmSheetDialog.dismiss()
            }
        }

        btmSheetDialog.show()
    }

    //Fungsi Simpan Data ke DB
    fun saveDataToDb(mapel: String, kelas: String, waktu: String) {
        val values = ContentValues()

        values.put("mapel", mapel)
        values.put("kelas", kelas)
        values.put("waktu", waktu)
        values.put("status", "0")
        values.put("catatan", "0")

        dbHelper.saveNewSchedule(values)

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

    //Fungsi Pilih Waktu

    @RequiresApi(Build.VERSION_CODES.N)
    fun selectTime() {
        val datePicker = DatePickerDialog(requireContext(), null, 2021, 11, 1)
        datePicker.show()

        datePicker.setOnDateSetListener { view, year, month, dayOfMonth ->
            edtWaktu.setText("$dayOfMonth-$month-$year")
        }
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