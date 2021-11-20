package com.example.pengingatjadwal.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import com.example.pengingatjadwal.Fragment.FragmentBeranda
import com.example.pengingatjadwal.Fragment.FragmentJadwal
import com.example.pengingatjadwal.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {

    // Variabel View
    lateinit var botNavMain: BottomNavigationView
    lateinit var ivPopUpMenu: ImageView
    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        /*ivPopUpMenu.setOnClickListener {
            showPopUp(ivPopUpMenu)
        }*/


    }

    //Fungsi inisialisasi View
    private fun initView() {
        botNavMain = findViewById(R.id.bot_nav_main)
        ivPopUpMenu = findViewById(R.id.iv_pop_up_menu)

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


    //Fungsi PopUp Menu
    fun showPopUp(v : View) {
        val popup = PopupMenu(this, v)

        popup.inflate(R.menu.menu_pop_menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.menu_bantuan -> run {
                    val intentMenuBantuan = Intent(applicationContext, BantuanActivity::class.java)
                    startActivity(intentMenuBantuan)
                }
                R.id.menu_tentang -> {}
            }
            true
        }
        popup.show()

    }
}