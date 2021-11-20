package com.example.pengingatjadwal.Fragment.Jadwal

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
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
import kotlin.collections.ArrayList





class FragmentBerlangsung: Fragment(), RecSemuaJadwalItem, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
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
    lateinit var jadwalModel: JadwalModel



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
        checkEmptySchedule()

        fabTambah = rootView.findViewById(R.id.fab_tambah_jadwal)
        fabTambah.setOnClickListener {
            addDataDialog(JadwalModel())

            setRecData()
        }

        chipSemua.setOnClickListener {
            listJadwal = dbHelper.getAllSchedule()
            setRecData()
        }

        chipSenin.setOnClickListener { scheduleMonday() }
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
    fun checkEmptySchedule() {
        if (listJadwal.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }
    }

    //Fungsi Hapus Jadwal Saat Lewat Waktunya
    //TODO 1
    fun deletePastSchedule() {
        listJadwal = dbHelper.getAllSchedule()
        recAdapter = RecBerlangsungAdapter(listJadwal, this, 2)
        if (Calendar.getInstance().after(jadwalModel.tanggal)) {
            onDelete(jadwalModel.id)
        }

    }

    //Fungsi Membuat BottomSheet Dialog untuk Menambahkan Data
    fun addDataDialog(jadwalModel: JadwalModel) {
        val btmSheetView = LayoutInflater.from(requireContext())
            .inflate(R.layout.view_btm_sheet_tambah_jadwal, null, false)
        val btmSheetDialog = BottomSheetDialog(requireContext())

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
                selectDateMaterial()
            }
        }

        edtWaktu.isClickable = true
        edtWaktu.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectTimeMaterial()
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
                    dbHelper.updateScheduleBeranda(
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

                isAllFieldsChecked = checkAllFields()

                if (isAllFieldsChecked) {
                    saveDataToDb(
                        edtMapel.text.toString(),
                        edtKelas.text.toString(),
                        hari,
                        edtTanggal.text.toString(),
                        edtWaktu.text.toString()
                    )
                    saveDataToDbBeranda(
                        edtMapel.text.toString(),
                        edtKelas.text.toString(),
                        hari,
                        edtTanggal.text.toString(),
                        edtWaktu.text.toString()
                    )
                    setRecData()
                    checkEmptySchedule()
                    btmSheetDialog.dismiss()
                }
            }
        }
        btmSheetDialog.show()
    }

    //Fungsi Intent Aplikasi Alarm
    fun setAlarmClock() {
        val i = Intent(AlarmClock.ACTION_SET_ALARM)
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "${jadwalModel.mapel}")
        i.putExtra(AlarmClock.EXTRA_HOUR, formKalender[Calendar.HOUR_OF_DAY])
        i.putExtra(AlarmClock.EXTRA_MINUTES, formKalender[Calendar.MINUTE])
        startActivity(i)
    }

    //Fungsi Mengecek Masukan Pengguna
    fun checkAllFields(): Boolean {

        listJadwal = dbHelper.getTimeByDate(edtTanggal.text.toString())

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

        if(edtTanggal.text.toString().equals(listJadwal)) {
            if (edtWaktu.text.toString().equals(dbHelper.getTimeByDate(edtTanggal.text.toString()))) {
                edtWaktu.setError("Sudah Ada Jadwal di Waktu Ini")
                return false
            }
        }


        return true
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

    //Fungsi Pilih Mapel
    fun selectSubject() {
        val mapel = arrayOf("Agama", "PPKN", "Matematika", "B. Indonesia", "B. Inggris", "IPA", "IPS", "SBK", "Penjaskes", "Mulok")
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


    //Fungsi Pilih Hari MaterialDatePicker
    fun selectDateMaterial() {
        val kalender = Calendar.getInstance()

        val MAX_SELECTABLE_DATE_IN_FUTURE = 365

        val datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
            this,
            kalender.get(Calendar.YEAR),
            kalender.get(Calendar.MONTH),
            kalender.get(Calendar.DAY_OF_MONTH)
        )

        val weekdays = ArrayList<Calendar>()
        val day = Calendar.getInstance()
        for (i in 0 until MAX_SELECTABLE_DATE_IN_FUTURE) {
            if (day[Calendar.DAY_OF_WEEK] !== Calendar.SATURDAY && day[Calendar.DAY_OF_WEEK] !== Calendar.SUNDAY) {
                val d = day.clone() as Calendar
                weekdays.add(d)
            }
            day.add(Calendar.DATE, 1)
        }
        val weekdayDays: Array<Calendar> = weekdays.toArray(arrayOfNulls(weekdays.size))
        datePickerDialog.setSelectableDays(weekdayDays)

        datePickerDialog.minDate = Calendar.getInstance()
        datePickerDialog.version = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2
        datePickerDialog.setAccentColor(Color.parseColor("#136BAF"))
        datePickerDialog.show(requireFragmentManager(), "DatePickerDialog")

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


    //Fungsi Pilih Waktu Material
    fun selectTimeMaterial() {
        val kalender = Calendar.getInstance()

        val timePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
            this,
            kalender.get(Calendar.HOUR_OF_DAY),
            kalender.get(Calendar.MINUTE),
            kalender.get(Calendar.SECOND),
            true)

        timePicker.setMinTime(7,0,0)
        timePicker.setMaxTime(14,0,0)
        timePicker.setAccentColor(Color.parseColor("#136BAF"))
        timePicker.version = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2
        timePicker.show(requireFragmentManager(), "TimePickerDialog")
    }

    //Fungsi Mendapatkan Jadwal Berdasarkan Hari
    fun getScheduleByDay(day: String) {
        listJadwal = dbHelper.getAllScheduleByDay(day)
        setResetRecData()
        checkEmptySchedule()
    }

    //Fungsi Menampilkan Jadwal Hari Senin
    fun scheduleMonday() {
        getScheduleByDay("Monday")

        if (listJadwal.size == null) {
            getScheduleByDay("Senin")
        }

    }

    //Fungsi Menampilkan Jadwal Hari Selasa
    fun scheduleTuesday() {
        getScheduleByDay("Tuesday")

        if (listJadwal.size == null) {
            getScheduleByDay("Selasa")
        }
    }

    //Fungsi Menampilkan Jadwal Hari Rabu
    fun scheduleWednesday() {
        getScheduleByDay("Wednesday")
        if (listJadwal.size == null) {
            getScheduleByDay("Rabu")
        }

    }

    //Fungsi Menampilkan Jadwal Hari Kamis
    fun scheduleThursday() {
        getScheduleByDay("Thursday")

        if (listJadwal.size == null) {
            getScheduleByDay("Kamis")
        }
    }

    //Fungsi Menampilkan Jadwal Hari Jumat
    fun scheduleFriday() {
        getScheduleByDay("Friday")
        if (listJadwal.size == null) {
            getScheduleByDay("Jumat")
        }

    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: Int) {
        dbHelper.deleteSchedule(id)
        setRecData()
        checkEmptySchedule()
    }

    //Fungsi Perbarui Data (sumber: Interface)
    override fun onUpdate(jadwalModel: JadwalModel) {
        addDataDialog(jadwalModel)
    }

    override fun onDateSet(
        view: com.wdullaer.materialdatetimepicker.date.DatePickerDialog?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) {
        val simpleDateFormat = SimpleDateFormat("EEEE")
        val tanggal = Date(year, monthOfYear, dayOfMonth - 1)
        val hariString = simpleDateFormat.format(tanggal)

        edtTanggal.setText("$dayOfMonth-${monthOfYear+1}-$year")
        hari = hariString
        edtHari.setText(hariString)

        formKalender[Calendar.DAY_OF_MONTH] = dayOfMonth
        formKalender[Calendar.MONTH] = monthOfYear
        formKalender[Calendar.YEAR] = year
    }

    override fun onTimeSet(
        view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog?,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) {
        edtWaktu.setText("$hourOfDay:$minute")

        formKalender[Calendar.HOUR_OF_DAY] = hourOfDay
        formKalender[Calendar.MINUTE] = minute
        formKalender[Calendar.SECOND] = 0
        formKalender[Calendar.MILLISECOND] = 0
    }

}