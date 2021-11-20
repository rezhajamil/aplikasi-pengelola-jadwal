package com.example.pengingatjadwal.Alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pengingatjadwal.Activity.MainActivity
import com.example.pengingatjadwal.R

class AlarmReceiver: BroadcastReceiver() {
    //Membuat notifikasi
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MainActivity::class.java)
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        intent!!.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, 0)

        val builder = NotificationCompat.Builder(context!!, "ChannelPengingatJadwalMengajar")
            .setSmallIcon(R.drawable.ic_kelas)
            .setContentTitle("Persiapan Mengajar")
            .setContentText("Anda memiliki jadwal mengajar hari ini, tekan disini untuk melihatnya")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(0, 500, 1000))
            .setContentIntent(pendingIntent)
            .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ context.packageName + "/" + R.raw.alarm))



        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, builder.build())

    }
}