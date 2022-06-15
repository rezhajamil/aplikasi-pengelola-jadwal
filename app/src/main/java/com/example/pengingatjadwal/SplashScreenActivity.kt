package com.example.pengingatjadwal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.pengingatjadwal.Activity.IntroActivity
import com.example.pengingatjadwal.Activity.LoginActivity


class SplashScreenActivity : AppCompatActivity() {
    lateinit var ivLogo: ImageView
    lateinit var tvLogo: TextView
    lateinit var fromTop: Animation
    lateinit var fromBottom: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        ivLogo = findViewById(R.id.iv_logo)
        tvLogo = findViewById(R.id.tv_logo)
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation)
        fromTop = AnimationUtils.loadAnimation(this, R.anim.from_top_animation)

        ivLogo.animation = fromTop
        tvLogo.animation = fromBottom

        //Membuat Timer Lottie
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                val intent: Intent = Intent(applicationContext, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}