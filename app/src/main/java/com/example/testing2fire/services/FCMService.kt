package com.example.testing2fire.services

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testing2fire.MyApp
import com.example.testing2fire.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("dbug", message.toString())
        showNotification(message)
    }

    private fun showNotification(message: RemoteMessage) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this,MyApp.NOTIFICATION_CHANNEL_ID).setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.baseline_notification).setAutoCancel(true).build()

        notificationManager.notify(1,notification)

    }
}