package com.example.pengingatjadwal.Fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecBerandaAdapter
import com.example.pengingatjadwal.Alarm.AlarmHelper
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FragmentBeranda: Fragment() {

    //Variabel View
    lateinit var rootView: View
    lateinit var tvHari: TextView
    lateinit var tvTanggal: TextView
    lateinit var tvHalo: TextView
    lateinit var recBeranda: RecyclerView
    lateinit var llKosong: LinearLayout

    //Variabel
    lateinit var recAdapter: RecBerandaAdapter
    lateinit var listJadwalSekarang: ArrayList<JadwalModel>
    lateinit var listJadwalSekarangBeranda: MutableList<JadwalModel>
    lateinit var formKalender: Calendar
    lateinit var alarmHelper: AlarmHelper
    var calendar: Calendar = Calendar.getInstance()
    var isAllFieldsChecked: Boolean = false
    val formatNamaHari = SimpleDateFormat("EEEE")
    val formatTanggal = SimpleDateFormat("dd MMMM yyyy")
    val dateFormat = SimpleDateFormat("d-M-yyyy")
    val timeFormat = SimpleDateFormat("HH:mm")
    var hariIni = Date()

    private lateinit var mDatabaseQuery: Query


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_beranda, null, false)

        initView()
        getAllJadwalFromFirebase()
        checkEmptySchedule()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {
        recBeranda = rootView.findViewById(R.id.rec_beranda)
        tvHari = rootView.findViewById(R.id.tv_hari_beranda)
        tvTanggal = rootView.findViewById(R.id.tv_tanggal_beranda)
        tvHalo=rootView.findViewById(R.id.tv_halo)
        llKosong = rootView.findViewById(R.id.ll_jadwal_kosong_beranda)

        alarmHelper = AlarmHelper(requireActivity())
        formKalender = Calendar.getInstance()
        formKalender.timeInMillis = System.currentTimeMillis()

        recBeranda.layoutManager = LinearLayoutManager(requireContext())

        setDayName()
        val preferences: SharedPreferences? = context?.getSharedPreferences("User",0)
        val email= preferences?.getString("email","")
        tvTanggal.text = "${formatTanggal.format(hariIni)}"
        tvHalo.text="Halo ${email}".uppercase()

        getAllJadwalFromFirebase()
    }

    //Fungsi Ubah Nama Hari
    fun setDayName() : String {
        var hari: String = ""

        if (formatNamaHari.format(hariIni).equals("Monday") || formatNamaHari.format(hariIni).equals("Senin")) { tvHari.text = "Senin" }
        if (formatNamaHari.format(hariIni).equals("Tuesday") || formatNamaHari.format(hariIni).equals("Selasa")) { tvHari.text = "Selasa" }
        if (formatNamaHari.format(hariIni).equals("Wednesday") || formatNamaHari.format(hariIni).equals("Rabu")) { tvHari.text = "Rabu" }
        if (formatNamaHari.format(hariIni).equals("Thursday") || formatNamaHari.format(hariIni).equals("Kamis")) { tvHari.text = "Kamis" }
        if (formatNamaHari.format(hariIni).equals("Friday") || formatNamaHari.format(hariIni).equals("Jumat")) { tvHari.text = "Jumat" }
        if (formatNamaHari.format(hariIni).equals("Saturday") || formatNamaHari.format(hariIni).equals("Sabtu")) { tvHari.text = "Sabtu" }
        if (formatNamaHari.format(hariIni).equals("Sunday") || formatNamaHari.format(hariIni).equals("Minggu")) { tvHari.text = "Minggu" }
        return hari
    }

    private fun getAllJadwalFromFirebase() {
        mDatabaseQuery= FirebaseDatabase.getInstance().getReference("Jadwal")
        listJadwalSekarang= ArrayList<JadwalModel>()
        var today=dateFormat.format(hariIni)
        mDatabaseQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listJadwalSekarang.clear()
                for(getdataSnapshot in snapshot.children){
                    var jadwal=getdataSnapshot.getValue(JadwalModel::class.java)
                    Log.v("beranda", (jadwal?.tanggal==today).toString())
                    Log.v("beranda", today)
                    if (jadwal?.tanggal==today){
                        listJadwalSekarang.add(jadwal!!)
                    }

                }
                if (recBeranda!=null){
                    recAdapter = RecBerandaAdapter(listJadwalSekarang){data->
                        val btnDoneScheduleView = LayoutInflater.from(activity)
                            .inflate(R.layout.view_btm_sheet_selesaikan_jadwal, null, false)

                        val edtCatatan = btnDoneScheduleView.findViewById<EditText>(R.id.edt_catatan_view_btm_selesai_mengajar)
                        val mbtSelesai = btnDoneScheduleView.findViewById<MaterialButton>(R.id.mbt_selesai_view_btm_selesai_mengajar)

                        val btmSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)

                        btmSheetDialog.setContentView(btnDoneScheduleView)
                        btmSheetDialog.show()

                        mbtSelesai.setOnClickListener {
                            if(edtCatatan.length() == 0) {
                                edtCatatan.error = "Wajib Diisi"
                            } else {
                                val refJadwal= FirebaseDatabase.getInstance().getReference("Jadwal")
                                var jadwal=JadwalModel()
                                jadwal.id=data.id
                                jadwal.kegiatan=data.kegiatan
                                jadwal.tim=data.tim
                                jadwal.hari=data.hari
                                jadwal.tanggal=data.tanggal
                                jadwal.waktu=data.waktu
                                jadwal.status=1
                                jadwal.catatan=edtCatatan.text.toString()

                                refJadwal.child(data.id).setValue(jadwal)

                                btmSheetDialog.dismiss()
                            }
                        }
                    }
                    recBeranda.adapter= recAdapter
                }
                checkEmptySchedule()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun checkEmptySchedule() {
        if (listJadwalSekarang.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }
    }


}