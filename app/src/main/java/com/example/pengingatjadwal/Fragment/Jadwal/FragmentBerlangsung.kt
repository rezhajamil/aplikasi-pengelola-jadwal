package com.example.pengingatjadwal.Fragment.Jadwal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecBerlangsungAdapter
import com.example.pengingatjadwal.Adapter.RecSemuaJadwalItem
import com.example.pengingatjadwal.Alarm.AlarmHelper
import com.example.pengingatjadwal.Database.DbHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class FragmentBerlangsung: Fragment(), RecSemuaJadwalItem {
    //Variabel View
    lateinit var rootView: View
    lateinit var fabTambah: FloatingActionButton
    lateinit var recBerlangsung: RecyclerView
    lateinit var edtMapel: EditText
    lateinit var edtKelas: EditText
    lateinit var edtTanggal: EditText
    lateinit var edtWaktu: EditText
    lateinit var edtHari: EditText
    lateinit var chipSemua: Chip
    lateinit var chipSenin: Chip
    lateinit var chipSelasa: Chip
    lateinit var chipRabu: Chip
    lateinit var chipKamis: Chip
    lateinit var chipJumat: Chip
    lateinit var llKosong: LinearLayout

    //Variabel
    lateinit var dbHelper: DbHelper
    lateinit var recAdapter: RecBerlangsungAdapter
    lateinit var listJadwal: MutableList<JadwalModel>
    lateinit var formKalender: Calendar
    lateinit var alarmHelper: AlarmHelper
    lateinit var hari: String
    var isAllFieldsChecked: Boolean = false


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

        chipSemua = rootView.findViewById(R.id.chip_semua)
        chipSenin = rootView.findViewById(R.id.chip_senin)
        chipSelasa = rootView.findViewById(R.id.chip_selasa)
        chipRabu = rootView.findViewById(R.id.chip_rabu)
        chipKamis = rootView.findViewById(R.id.chip_kamis)
        chipJumat = rootView.findViewById(R.id.chip_jumat)
        llKosong = rootView.findViewById(R.id.ll_jadwal_kosong_berlangsung)
        recBerlangsung = rootView.findViewById(R.id.rec_berlangsung)

        recBerlangsung.layoutManager = LinearLayoutManager(context)

        listJadwal = dbHelper.getAllSchedule()


        setRecData()
        cekJadwalKosong()

        fabTambah = rootView.findViewById(R.id.fab_tambah_jadwal)
        fabTambah.setOnClickListener {
            addDataDialog(JadwalModel())

            setRecData()
        }

        chipSemua.setOnClickListener {
            listJadwal = dbHelper.getAllSchedule()
            setRecData()
        }

        chipSenin.setOnClickListener {
            Toast.makeText(requireContext(), "Chip Senin", Toast.LENGTH_SHORT).show()
            scheduleMonday()
        }
        chipSelasa.setOnClickListener { scheduleTuesday() }
        chipRabu.setOnClickListener { scheduleWednesday() }
        chipKamis.setOnClickListener { scheduleThursday() }
        chipJumat.setOnClickListener { scheduleFriday() }
    }

    //Fungsi Ngeset Data ke Adapter
    fun setRecData() {
        listJadwal = dbHelper.getAllSchedule()
        recAdapter = RecBerlangsungAdapter(listJadwal, this, 2)
        recBerlangsung.adapter = recAdapter
    }

    //Fungsi Reset Data ke Adapter
    fun setResetRecData() {
        recAdapter = RecBerlangsungAdapter(listJadwal, this, 2)
        recBerlangsung.adapter = recAdapter
    }

    //Fungsi Cek Jadwal Kosong
    fun cekJadwalKosong() {
        if (listJadwal.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }
    }

    //Fungsi Membuat BottomSheet Dialog untuk Menambahkan Data
    fun addDataDialog(jadwalModel: JadwalModel) {
        val btmSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_btm_sheet_tambah_jadwal, null, false)
        val btmSheetDialog = BottomSheetDialog(requireContext())
        var isError: Boolean = false

        edtMapel = btmSheetView.findViewById(R.id.edt_mapel_tambah_jadwal)
        edtKelas = btmSheetView.findViewById(R.id.edt_kelas_tambah_jadwal)
        edtTanggal = btmSheetView.findViewById(R.id.edt_tanggal_tambah_jadwal)
        edtWaktu = btmSheetView.findViewById(R.id.edt_waktu_tambah_jadwal)
        edtHari = btmSheetView.findViewById(R.id.edt_hari_tambah_jadwal)
        val mbtTambah = btmSheetView.findViewById<MaterialButton>(R.id.btn_tambah_tambah_jadwal)

        edtMapel.inputType = InputType.TYPE_NULL
        edtKelas.inputType = InputType.TYPE_NULL
        edtTanggal.inputType = InputType.TYPE_NULL
        edtWaktu.inputType = InputType.TYPE_NULL

        btmSheetDialog.setContentView(btmSheetView)

        edtMapel.isClickable = true
        edtMapel.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectSubject()
            }
        }

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
            edtHari.setText(jadwalModel.hari)

            val jadwalBaru = JadwalModel(
                jadwalModel.id,
                edtMapel.text.toString(),
                edtKelas.text.toString(),
                edtTanggal.text.toString(),
                edtWaktu.text.toString(),
            )

            mbtTambah.setText("Perbarui")
            mbtTambah.setOnClickListener {
                isAllFieldsChecked = checkAllFields()

                if (isAllFieldsChecked) {
                    dbHelper.updateSchedule(
                        jadwalBaru.id,
                        edtMapel.text.toString(),
                        edtKelas.text.toString(),
                        edtHari.text.toString(),
                        edtTanggal.text.toString(),
                        edtWaktu.text.toString(),
                        "0",
                        "0")
                    setRecData()
                    alarmHelper.setAlarm(0, formKalender)
                    btmSheetDialog.dismiss()
                }
            }

        } else {
            mbtTambah.setOnClickListener {
                /*if(edtMapel.text.toString() == "") {
                    isError = true
                    edtMapel.setError("Wajib Diisi")
                }

                if(edtKelas.text.toString() == "") {
                    isError = true
                    edtKelas.setError("Wajib Diisi")
                }

                if(edtTanggal.text.toString() == "") {
                    isError = true
                    edtTanggal.setError("Wajib Diisi")
                }

                if(edtWaktu.text.toString() == "") {
                    isError = true
                    edtWaktu.setError("Wajib Diisi")
                }*/

                isAllFieldsChecked = checkAllFields()

                if (isAllFieldsChecked) {
                    saveDataToDb(
                        edtMapel.text.toString(),
                        edtKelas.text.toString(),
                        hari,
                        edtTanggal.text.toString(),
                        edtWaktu.text.toString()
                    )
                    setRecData()
                    btmSheetDialog.dismiss()
                }
            }
        }
        btmSheetDialog.show()
    }

    //Fungsi Mengecek Masukan Pengguna
    fun checkAllFields(): Boolean {
        if (edtMapel.length() == 0) {
            edtMapel.setError("Wajib Diisi")
            return false
        }

        if (edtKelas.length() == 0) {
            edtKelas.setError("Wajib Diisi")
            return false
        }

        if (edtTanggal.length() == 0) {
            edtTanggal.setError("Wajib Diisi")
            return false
        }

        if (edtWaktu.length() == 0) {
            edtWaktu.setError("Wajib Diisi")
            return false
        }

        return true
    }

    //Fungsi Simpan Data ke DB
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

        setRecData()
    }

    //Fungsi Pilih Mapel
    fun selectSubject() {
        val mapel = arrayOf("Agama", "PPKN", "Matematika", "Bahasa Indonesia", "IPA", "IPS", "SBK", "Penjaskes", "Mulok")
        val dialog = AlertDialog.Builder(requireContext())
        dialog
            .setTitle("Pilih Kelas")
            .setItems(mapel) { dialog, position ->
                Toast.makeText(requireContext(), mapel[position], Toast.LENGTH_SHORT).show()
                edtMapel.setText(mapel[position].toString())
            }
            .show()
    }

    //Fungsi Pilih Kelas
    fun selectClass() {
        val kelas = arrayOf("Kelas 1-A", "Kelas 1-B", "Kelas 1-C", "Kelas 1-D", "Kelas 2-A", "Kelas 2-B", "Kelas 2-C", "Kelas 2-D", "Kelas 3-A", "Kelas 3-B", "Kelas 3-C", "Kelas 3-D", "Kelas 4-A", "Kelas 4-B", "Kelas 4-C", "Kelas 4-D", "Kelas 5-A", "Kelas 5-B", "Kelas 5-C", "Kelas 5-D", "Kelas 6-A", "Kelas 6-B", "Kelas 6-C", "Kelas 6-D")
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

        val datePickerDialog = DatePickerDialog(requireContext(), {view, year, month, dayOfMonth ->
            val simpleDateFormat = SimpleDateFormat("EEEE")
            val tanggal = Date(year, month, dayOfMonth - 1)
            val hariString = simpleDateFormat.format(tanggal)

            edtTanggal.setText("$dayOfMonth-${month+1}-$year")
            hari = hariString
            edtHari.setText(hariString)

            formKalender[Calendar.DAY_OF_MONTH] = dayOfMonth
            formKalender[Calendar.MONTH] = month
            formKalender[Calendar.YEAR] = year
        },  kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DATE))

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
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


    fun getScheduleByDay(day: String) {
        listJadwal = dbHelper.getAllScheduleByDay(day)
        setResetRecData()
        cekJadwalKosong()
    }

    //Fungsi Menampilkan Jadwal Hari Senin
    fun scheduleMonday() {
        getScheduleByDay("Monday")
    }

    //Fungsi Menampilkan Jadwal Hari Selasa
    fun scheduleTuesday() {
        getScheduleByDay("Tuesday")
    }

    //Fungsi Menampilkan Jadwal Hari Rabu
    fun scheduleWednesday() {
        getScheduleByDay("Wednesday")
    }

    //Fungsi Menampilkan Jadwal Hari Kamis
    fun scheduleThursday() {
        getScheduleByDay("Thursday")
    }

    //Fungsi Menampilkan Jadwal Hari Jumat
    fun scheduleFriday() {
        getScheduleByDay("Friday")
    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: Int) {
        dbHelper.deleteSchedule(id)
        setRecData()
        cekJadwalKosong()
    }

    //Fungsi Perbauri Data (sumber: Interface)
    override fun onUpdate(jadwalModel: JadwalModel) {
        addDataDialog(jadwalModel)
    }

}