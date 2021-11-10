package com.example.pengingatjadwal.Jadwal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pengingatjadwal.R

class FragmentRiwayat: Fragment() {

    //Variabel View
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_riwayat, null, false)

        initView()
        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {

    }
}