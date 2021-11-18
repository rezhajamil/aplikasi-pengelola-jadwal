package com.example.pengingatjadwal.Alarm

import android.app.*
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.provider.AlarmClock
import android.widget.Toast
import com.example.pengingatjadwal.R
import java.util.*

class AlarmHelper(val activity: Activity) {

    val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ activity.packageName + "/" + R.raw.alarm)

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
    }



    //Fungsi membuat Channel Notifikasi
    fun createNotificationChannel(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nama: CharSequence = "ChannelPengingatJadwalMengajar"
            val deskripsi = "Channel Notifikasi untuk Pengingat Jadwal Pengajar"
            val kepentingan = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("ChannelPengingatJadwalMengajar", nama, kepentingan)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            channel.description = deskripsi
            channel.setSound(soundUri, audioAttributes)

            val  notificationManager = activity.getSystemService(
                NotificationManager::class.java
            )

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
            }
        }

    }
}