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
import android.widget.*
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
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FragmentBerlangsung: Fragment(), RecSemuaJadwalItem, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener {
    //Variabel View
    lateinit var rootView: View
    lateinit var fabTambah: FloatingActionButton
    lateinit var recBerlangsung: RecyclerView
    lateinit var tvJudul: TextView
    lateinit var edtKegiatan: EditText
    lateinit var edtTim: EditText
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
    lateinit var listJadwal: ArrayList<JadwalModel>
    lateinit var formKalender: Calendar
    lateinit var alarmHelper: AlarmHelper
    lateinit var hari: String
    var isAllFieldsChecked: Boolean = false
    lateinit var jadwalModel: JadwalModel

    //Firebase
    val refJadwal= FirebaseDatabase.getInstance().getReference("Jadwal")
    private lateinit var mDatabaseQuery: Query



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_berlangsung, null, false)

        initView()
        getAllJadwalFromFirebase()

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
        listJadwal=ArrayList<JadwalModel>()
        checkEmptySchedule()

        fabTambah = rootView.findViewById(R.id.fab_tambah_jadwal)
        fabTambah.setOnClickListener {
            addDataDialog(JadwalModel())
        }

        chipSemua.setOnClickListener {
            getAllJadwalFromFirebase()
        }

        chipSenin.setOnClickListener { getAllJadwalFromFirebaseByDay("Senin") }
        chipSelasa.setOnClickListener { getAllJadwalFromFirebaseByDay("Selasa") }
        chipRabu.setOnClickListener { getAllJadwalFromFirebaseByDay("Rabu") }
        chipKamis.setOnClickListener { getAllJadwalFromFirebaseByDay("Kamis") }
        chipJumat.setOnClickListener { getAllJadwalFromFirebaseByDay("Jumat") }
    }


    private fun getAllJadwalFromFirebase() {
        mDatabaseQuery= FirebaseDatabase.getInstance().getReference("Jadwal")
        mDatabaseQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listJadwal.clear()
                for(getdataSnapshot in snapshot.children){
                    var jadwal=getdataSnapshot.getValue(JadwalModel::class.java)
                    listJadwal.add(jadwal!!)
                }
                if (recBerlangsung!=null){
                    recAdapter = RecBerlangsungAdapter(listJadwal){
                        addDataDialog(it)
                    }
                    recBerlangsung.adapter= recAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,""+error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun getAllJadwalFromFirebaseByDay(day: String) {
        mDatabaseQuery= FirebaseDatabase.getInstance().getReference("Jadwal")
        mDatabaseQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listJadwal.clear()
                for(getdataSnapshot in snapshot.children){
                    var jadwal=getdataSnapshot.getValue(JadwalModel::class.java)
                    if (jadwal?.hari==day){
                        listJadwal.add(jadwal!!)
                    }
                }
                if (recBerlangsung!=null){
                    recAdapter = RecBerlangsungAdapter(listJadwal){
                        addDataDialog(it)
                    }
                    recBerlangsung.adapter= recAdapter
                }

                checkEmptySchedule()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,""+error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    //Fungsi Cek Jadwal Kosong
    fun checkEmptySchedule() {
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
        val btmSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

        edtKegiatan = btmSheetView.findViewById(R.id.edt_kegiatan_tambah_jadwal)
        edtTim = btmSheetView.findViewById(R.id.edt_tim_tambah_jadwal)
        edtTanggal = btmSheetView.findViewById(R.id.edt_tanggal_tambah_jadwal)
        edtWaktu = btmSheetView.findViewById(R.id.edt_waktu_tambah_jadwal)
        edtHari = btmSheetView.findViewById(R.id.edt_hari_tambah_jadwal)
        tvJudul = btmSheetView.findViewById(R.id.tv_judul_tambah_jadwal)
        val mbtTambah = btmSheetView.findViewById<MaterialButton>(R.id.btn_tambah_tambah_jadwal)

        edtKegiatan.inputType = InputType.TYPE_NULL
        edtTim.inputType = InputType.TYPE_NULL
        edtTanggal.inputType = InputType.TYPE_NULL
        edtWaktu.inputType = InputType.TYPE_NULL

        btmSheetDialog.setContentView(btmSheetView)

        edtKegiatan.isClickable = true
        edtKegiatan.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectActivity()
            }
        }

        edtTim.isClickable = true
        edtTim.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectTeam()
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

        if (jadwalModel.kegiatan != "") {
            edtKegiatan.setText(jadwalModel.kegiatan)
            edtTim.setText(jadwalModel.tim)
            edtTanggal.setText(jadwalModel.tanggal)
            edtWaktu.setText(jadwalModel.waktu)
            edtHari.setText(jadwalModel.hari)

            val jadwalBaru = JadwalModel(
                jadwalModel.id,
                edtKegiatan.text.toString(),
                edtTim.text.toString(),
                edtTanggal.text.toString(),
                edtWaktu.text.toString(),
            )

            tvJudul.setText("Perbarui")
            mbtTambah.setText("Perbarui")
            mbtTambah.setOnClickListener {
                isAllFieldsChecked = checkAllFields()

                if (isAllFieldsChecked) {
//                    dbHelper.updateSchedule(
//                        jadwalBaru.id,
//                        edtKegiatan.text.toString(),
//                        edtTim.text.toString(),
//                        edtHari.text.toString(),
//                        edtTanggal.text.toString(),
//                        edtWaktu.text.toString(),
//                        "0",
//                        "0")
//                    dbHelper.updateScheduleBeranda(
//                        jadwalBaru.id,
//                        edtKegiatan.text.toString(),
//                        edtTim.text.toString(),
//                        edtHari.text.toString(),
//                        edtTanggal.text.toString(),
//                        edtWaktu.text.toString(),
//                        "0",
//                        "0")
//                    setRecData()


                    alarmHelper.setAlarm(0, formKalender)
                    btmSheetDialog.dismiss()
                }
            }

        } else {
            mbtTambah.setOnClickListener {

                isAllFieldsChecked = checkAllFields()

                if (isAllFieldsChecked) {
                    saveDataToDb(
                        edtKegiatan.text.toString(),
                        edtTim.text.toString(),
                        hari,
                        edtTanggal.text.toString(),
                        edtWaktu.text.toString()
                    )
//                    saveDataToDbBeranda(
//                        edtKegiatan.text.toString(),
//                        edtTim.text.toString(),
//                        edtHari.text.toString(),
//                        edtTanggal.text.toString(),
//                        edtWaktu.text.toString()
//                    )
//                    setRecData()


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
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "${jadwalModel.kegiatan}")
        i.putExtra(AlarmClock.EXTRA_HOUR, formKalender[Calendar.HOUR_OF_DAY])
        i.putExtra(AlarmClock.EXTRA_MINUTES, formKalender[Calendar.MINUTE])
        startActivity(i)
    }

    //Fungsi Mengecek Masukan Pengguna
    fun checkAllFields(): Boolean {

        if (edtKegiatan.length() == 0) {
            edtKegiatan.setError("Wajib Diisi")
            return false
        }

        if (edtTim.length() == 0) {
            edtTim.setError("Wajib Diisi")
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

    //Fungsi Simpan Data ke DB tbJadwal Firebase
    fun saveDataToDb(kegiatan: String, tim: String, hari: String, tanggal: String, waktu: String) {
        var jadwal=JadwalModel()
        var id=refJadwal.push().key.toString()
        jadwal.id=id
        jadwal.kegiatan=kegiatan
        jadwal.tim=tim
        jadwal.hari=hari
        jadwal.tanggal=tanggal
        jadwal.waktu=waktu
        jadwal.status=0
        jadwal.catatan=""

        refJadwal.child(id).setValue(jadwal)
        alarmHelper.setAlarm(0, formKalender)
    }

    //Fungsi Pilih Kegiatan
    fun selectActivity() {
        val kegiatan = arrayOf("Pasang Baru","Gangguan","Briefing Pagi","Briefing Sore")
        val builderDialog = AlertDialog.Builder(requireContext())
        builderDialog
            .setTitle("Pilih Kegiatan")
            .setItems(kegiatan) { dialog, position ->
                edtKegiatan.setText(kegiatan[position].toString())
            }
        val dialog = builderDialog.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        dialog.show()
    }

    //Fungsi Pilih Tim
    fun selectTeam() {
        val tim = arrayOf("1","2","3","4","5","6","7","8","9","10")
        val builderDialog = AlertDialog.Builder(requireContext())
        builderDialog
            .setTitle("Pilih Tim")
            .setItems(tim) { dialog, position ->
                edtTim.setText(tim[position].toString())
            }
        val dialog = builderDialog.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.rounded_bg)
        dialog.show()
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

    //Fungsi Pilih Waktu Material
    fun selectTimeMaterial() {
        val kalender = Calendar.getInstance()

        val timePicker = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
            this,
            kalender.get(Calendar.HOUR_OF_DAY),
            kalender.get(Calendar.MINUTE),
            kalender.get(Calendar.SECOND),
            true)

//        timePicker.setMinTime(7,0,0)
//        timePicker.setMaxTime(14,0,0)
        timePicker.setAccentColor(Color.parseColor("#136BAF"))
        timePicker.version = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.Version.VERSION_2
        timePicker.show(requireFragmentManager(), "TimePickerDialog")
    }

    //Fungsi Hapus Data (sumber: Interface)
    override fun onDelete(id: String) {
        dbHelper.deleteSchedule(id)
        checkEmptySchedule()
        alarmHelper.cancelAlarm(0, requireActivity())
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

        if (hari.equals("Monday")) { edtHari.setText("Senin") }
        if (hari.equals("Tuesday")) { edtHari.setText("Selasa") }
        if (hari.equals("Wednesday")) { edtHari.setText("Rabu") }
        if (hari.equals("Thursday")) { edtHari.setText("Kamis") }
        if (hari.equals("Friday")) { edtHari.setText("Jumat") }


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
        edtWaktu.setText(String.format("%02d:%02d", hourOfDay, minute))

        formKalender[Calendar.HOUR_OF_DAY] = hourOfDay
        formKalender[Calendar.MINUTE] = minute
        formKalender[Calendar.SECOND] = 0
        formKalender[Calendar.MILLISECOND] = 0
    }

}