package com.example.pengingatjadwal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.pengingatjadwal.Fragment.FragmentBeranda
import com.example.pengingatjadwal.Fragment.FragmentJadwal
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    // Variabel View
    lateinit var botNavMain: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    //Fungsi inisialisasi View
    private fun initView() {
        botNavMain = findViewById(R.id.bot_nav_main)
        botNavMain.setOnItemSelectedListener(this)

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentBeranda()).commit()

    }

    //Fungsi menu Bot Nav Menu
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        when (item.itemId) {
            R.id.menu_beranda -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentBeranda()).commit()
            }
            R.id.menu_jadwal -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentJadwal()).commit()
            }
        }

        return false
    }

}