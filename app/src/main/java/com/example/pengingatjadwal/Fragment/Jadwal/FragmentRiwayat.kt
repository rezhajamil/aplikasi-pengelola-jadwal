package com.example.pengingatjadwal.Fragment.Jadwal

import android.app.Fragment
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengingatjadwal.Adapter.RecRiwayatAdapter
import com.example.pengingatjadwal.Model.JadwalModel
import com.example.pengingatjadwal.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*

class FragmentRiwayat: androidx.fragment.app.Fragment() {

    //Variabel View
    lateinit var rootView: View
    lateinit var recRiwayat: RecyclerView
    lateinit var llKosong: LinearLayout
    lateinit var srcViewRiwayat: androidx.appcompat.widget.SearchView

    //Variabel
    lateinit var recAdapter: RecRiwayatAdapter
    lateinit var listRiwayatJadwal: ArrayList<JadwalModel>
    lateinit var listRiwayatJadwalNew: ArrayList<JadwalModel>

    private lateinit var mDatabaseQuery: Query

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_riwayat, null, false)

        initView()

        getAllRiwayatJadwalFromFirebase()

        checkEmptyHistory()
        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {

        recRiwayat= rootView.findViewById(R.id.rec_riwayat)
        llKosong = rootView.findViewById(R.id.ll_jadwal_kosong_riwayat)
        srcViewRiwayat = rootView.findViewById(R.id.src_view_jadwal_riwayat)

        recRiwayat.layoutManager=LinearLayoutManager(requireContext())

        listRiwayatJadwal = ArrayList<JadwalModel>()
        listRiwayatJadwalNew = ArrayList<JadwalModel>()

        getAllRiwayatJadwalFromFirebase()

        //Fungsi Pada Search View
        srcViewRiwayat.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.v("riwayat33","aa")
                listRiwayatJadwalNew=searchHistorySchedule(newText!!)

                Log.v("riwayat33",listRiwayatJadwalNew.toString())
                recRiwayat.adapter=RecRiwayatAdapter(listRiwayatJadwalNew){}

                if (listRiwayatJadwalNew.size == 0) {
                    llKosong.visibility = View.VISIBLE
                } else {
                    llKosong.visibility = View.GONE
                }
                return true
            }

        })
    }

    //Fungsi Mencari Riwayat Jadwal
    fun searchHistorySchedule(tim: String): ArrayList<JadwalModel> {
        var listRiwayatJadwalNew: ArrayList<JadwalModel>
        if (tim.equals("")){
            listRiwayatJadwalNew=listRiwayatJadwal
        }else{
            listRiwayatJadwalNew= listRiwayatJadwal.filter { s->s.tim==tim } as ArrayList<JadwalModel>
        }

        return listRiwayatJadwalNew
    }

    private fun getAllRiwayatJadwalFromFirebase() {
        mDatabaseQuery= FirebaseDatabase.getInstance().getReference("Jadwal")
        listRiwayatJadwal= ArrayList<JadwalModel>()
        mDatabaseQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listRiwayatJadwal.clear()
                for (getdataSnapshot in snapshot.children) {
                    var jadwal = getdataSnapshot.getValue(JadwalModel::class.java)
                    if (jadwal?.status == 1) {
                        listRiwayatJadwal.add(jadwal!!)
                    }
                }
                if (recRiwayat != null) {
                    recAdapter = RecRiwayatAdapter(listRiwayatJadwal) { data ->
                        val btmSheetCatatanView = LayoutInflater.from(activity)
                            .inflate(R.layout.view_btm_sheet_catatan, null, false)

                        val edtNotes =
                            btmSheetCatatanView.findViewById<TextView>(R.id.edt_catatan_view_btm_sheet_catatan)
                        val mbtClose =
                            btmSheetCatatanView.findViewById<TextView>(R.id.mbt_tutup_view_btm_sheet_catatan)

                        edtNotes.inputType = InputType.TYPE_NULL

                        edtNotes.text = data.catatan

                        val btmSheetCatatan =
                            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
                        btmSheetCatatan.setContentView(btmSheetCatatanView)
                        btmSheetCatatan.show()

                        mbtClose.setOnClickListener {
                            btmSheetCatatan.dismiss()
                        }
                    }

                    Log.v("riwayat",listRiwayatJadwal.toString())
                    recRiwayat.adapter=recAdapter

                    checkEmptyHistory()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error "+error.toString(),Toast.LENGTH_LONG).show()
            }
        })
    }

    fun checkEmptyHistory() {
        if (listRiwayatJadwal.size == 0) {
            llKosong.visibility = View.VISIBLE
        } else {
            llKosong.visibility = View.GONE
        }
    }
}
