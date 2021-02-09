package com.lokoheimer.promise

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


class MyNotificationPublisher : BroadcastReceiver() {
    companion object{
        var NOTIFICATION_ID = "notification-id"
        var NOTIFICATION = "notification"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        var notificationManager : NotificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
        var notification : Notification? = intent!!.getParcelableExtra<Notification>(NOTIFICATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                MainActivity.NOTIFICATION_CHANNEL_ID,
                "Main Channel",
                importance)
            notificationManager!!.createNotificationChannel(notificationChannel)
        }
        var id = intent.getIntExtra(NOTIFICATION_ID,0)
        notificationManager!!.notify(id,notification)

    }

}