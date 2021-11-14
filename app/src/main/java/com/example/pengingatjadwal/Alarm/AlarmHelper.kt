package com.example.pengingatjadwal.Alarm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import java.util.*

class AlarmHelper(val activity: Activity) {

    //Fungsi mengatur alarm
    fun setAlarm(id: Int, calendar: Calendar) {

        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, 0)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

        Toast.makeText(activity, "Pengingat telah Diatur", Toast.LENGTH_SHORT).show()
    }

    //Fungsi membuat Channel Notifikasi
    fun createNotificationChannel(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nama: CharSequence = "ChannelPengingatJadwalMengajar"
            val deskripsi = "Channel Notifikasi untuk Pengingat Jadwal Pengajar"
            val kepentingan = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ChannelPengingatJadwalMengajar", nama, kepentingan)

            channel.description = deskripsi

            val  notificationManager = activity.getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(channel)
        }

    }
}