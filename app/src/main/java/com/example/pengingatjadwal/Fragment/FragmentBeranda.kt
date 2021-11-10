package com.example.pengingatjadwal.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pengingatjadwal.R

class FragmentBeranda: Fragment() {

    // Variabel View
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_beranda, null, false)

        initView()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {

    }
}