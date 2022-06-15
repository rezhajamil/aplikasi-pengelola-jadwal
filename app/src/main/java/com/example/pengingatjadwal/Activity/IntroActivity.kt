package com.example.pengingatjadwal.Activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.pengingatjadwal.Adapter.BantuanViewPagerAdapter
import com.example.pengingatjadwal.Model.ScreenItem
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout

class IntroActivity : AppCompatActivity() {
    //Inisialisasi Variabel View
    lateinit var viewScreenPager: ViewPager
    lateinit var tabIndicator: TabLayout
    lateinit var btnLanjut: MaterialButton
    lateinit var btnMulai: Button
    lateinit var btnAnimation: Animation

    //Inisialisasi Variabel
    lateinit var bantuanViewPagerAdapter: BantuanViewPagerAdapter
    var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bantuan)

        if (restorePrefsData()) {
            if (checkLogin()){
                val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent: Intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        tabIndicator = findViewById(R.id.tab_indicator)
        btnLanjut = findViewById(R.id.btn_lanjut)
        btnMulai = findViewById(R.id.btn_mulai)
        btnAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_mulai_animation)


        //Membuat dan mengisi list screen
        val listScreenItem: MutableList<ScreenItem> = mutableListOf()
        listScreenItem.add(
            ScreenItem(
                "Kelola Jadwal",
                "Tambah, Ubah, dan Hapus Data Jadwal Kantor Anda",
                "*Agar aplikasi berjalan lancar harap mengelola jadwal terlebih dahulu",
                R.drawable.ic_kelola_jadwal
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Tambah Data",
                "Pastikan Mengisi Semua Data Jadwal Dengan Benar",
                "*Data akan ditambah jika semua data terisi dengan benar",
                R.drawable.ic_tambah_jadwal
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Pengingat Jadwal",
                "Notifikasi Alarm Pengingat Jadwal Kantor Anda",
                "*Fitur ini bekerja otomatis seiring dengan data jadwal",
                R.drawable.ic_alarm
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Lihat Riwayat",
                "Catat dan Lihat Setiap Riwayat Kegiatan Yang Dilakukan",
                "*Setiap jadwal kantor yang diselesaikan akan otomatis tercatat",
                R.drawable.ic_history
            )
        )

        viewScreenPager = findViewById(R.id.view_pager_screen)
        bantuanViewPagerAdapter = BantuanViewPagerAdapter(this, listScreenItem)
        viewScreenPager.setAdapter(bantuanViewPagerAdapter)

        tabIndicator!!.setupWithViewPager(viewScreenPager)

        btnLanjut.setOnClickListener(View.OnClickListener {
            position = viewScreenPager.currentItem
            if (position < listScreenItem.size) {
                position++
                viewScreenPager.currentItem = position
            }

            if (position == listScreenItem.size - 1) {
                loadLastScreen()
            }
        })


        //Fungsi Tab saat layar digeser
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == listScreenItem.size - 1) {
                    loadLastScreen()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        btnMulai.setOnClickListener(View.OnClickListener {
            val intent: Intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            savePrefsData()
            finish()
        })

    }

    //Fungsi untuk menyembunyikan Button Lanjut dan Indikator lalu menampilkan Button Mulai
    private fun loadLastScreen() {
        btnLanjut.visibility = View.INVISIBLE
        tabIndicator.visibility = View.INVISIBLE
        btnMulai.visibility = View.VISIBLE
        btnMulai.animation = btnAnimation
    }

    //Fungsi mengecek Shared Preferences untuk menyimpan informasi bahwa user sudah membuka Intro
    fun restorePrefsData(): Boolean {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isIntroOpenedBefore: Boolean = preferences.getBoolean("isIntroOpened", false)
        return isIntroOpenedBefore
    }

    //Fungsi mengecek Shared Preferences untuk menyimpan informasi bahwa user sudah Login
    fun checkLogin(): Boolean {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences("User",0)
        val email=preferences.getString("email","")
        if (email.equals("")){
            return false
        }
        return true
    }

    // Fungsi Shared Preferences untuk menyimpan informasi bahwa user sudah membuka Intro
    fun savePrefsData() {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("isIntroOpened", true)
        editor.commit()
    }


}