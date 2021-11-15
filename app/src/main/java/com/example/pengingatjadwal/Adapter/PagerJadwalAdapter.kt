package com.example.pengingatjadwal.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.pengingatjadwal.Fragment.Jadwal.FragmentBerlangsung
import com.example.pengingatjadwal.Fragment.Jadwal.FragmentRiwayat

class PagerJadwalAdapter(fm : FragmentManager): FragmentPagerAdapter(fm) {

    //Judul Tab
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Berjalan"
            1 -> "Riwayat"
            else -> "Berjalan"
        }
    }

    //Jumlah Tab
    override fun getCount(): Int {
        return 2
    }

    //Ganti Fragment
    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> FragmentBerlangsung()
            1 -> FragmentRiwayat()
            else -> FragmentBerlangsung()
        }

    }

}