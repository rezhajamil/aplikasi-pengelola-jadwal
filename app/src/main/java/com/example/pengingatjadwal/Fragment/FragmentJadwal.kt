package com.example.pengingatjadwal.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.pengingatjadwal.Adapter.PagerJadwalAdapter
import com.example.pengingatjadwal.R
import com.google.android.material.tabs.TabLayout

class FragmentJadwal: Fragment() {

    //Variabel View
    lateinit var rootView: View
    lateinit var tabJadwal: TabLayout

    //Variabel
    lateinit var viewPager: ViewPager
    lateinit var pagerAdapter: PagerJadwalAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_jadwal, null, false)

        initView()

        return rootView
    }

    //Fungsi Inisialisasi View
    private fun initView() {
        pagerAdapter = PagerJadwalAdapter(childFragmentManager)


        tabJadwal = rootView.findViewById(R.id.tab_jadwal)
        viewPager = rootView.findViewById(R.id.view_pager_jadwal)

        viewPager.adapter = pagerAdapter

        tabJadwal.setupWithViewPager(viewPager)

    }
}