package com.example.pengingatjadwal.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.example.pengingatjadwal.Adapter.BantuanViewPagerAdapter
import com.example.pengingatjadwal.Model.ScreenItem
import com.example.pengingatjadwal.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.util.ArrayList

class BantuanActivity : AppCompatActivity() {

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

        tabIndicator = findViewById(R.id.tab_indicator)
        btnLanjut = findViewById(R.id.btn_lanjut)
        btnMulai = findViewById(R.id.btn_mulai)
        btnAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.button_mulai_animation)


        //Membuat dan mengisi list screen
        val listScreenItem: MutableList<ScreenItem> = mutableListOf()
        listScreenItem.add(
            ScreenItem(
                "Kelola Produk",
                "Tambah, Ubah, dan Hapus Data Produk Dagangan Anda",
                "*Agar aplikasi berjalan lancar harap mengelola produk terlebih dahulu",
                R.drawable.ic_bantuan
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Tambah Data",
                "Pastikan mengisi semua data produk dengan benar",
                "*Tombol tambah akan aktif jika semua data terisi dengan benar",
                R.drawable.ic_bantuan
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Gunakan Barcode",
                "Gunakan Kamera Layaknya Barcode Scanner",
                "*Untuk menggunakan fitur ini harap izinkan aplikasi mengakses kamera",
                R.drawable.ic_bantuan
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Isi Keranjang ",
                "Buat Daftar Produk Yang Dibeli Pelanggan",
                "*Keranjang hanya dapat diisi dengan produk yang telah dikelola sebelumnya",
                R.drawable.ic_bantuan
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Hitung Cepat",
                "Total Harga Belanja Pelanggan Terhitung Otomatis",
                "*Total harga diperoleh dari jumlah harga satuan produk dikali banyaknya ",
                R.drawable.ic_bantuan
            )
        )
        listScreenItem.add(
            ScreenItem(
                "Lihat Riwayat",
                "Catat dan Lihat Setiap Transaksi Yang Dilakukan",
                "*Setiap transaksi yang diproses akan otomatis tercatat",
                R.drawable.ic_bantuan
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
        tabIndicator.addOnTabSelectedListener(object : OnTabSelectedListener {
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
            onBackPressed()
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
}