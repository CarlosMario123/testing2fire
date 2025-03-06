package com.example.testing2fire.core.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testing2fire.MainActivity
import com.example.testing2fire.R
import com.example.courierapp.core.datastore.PreferencesManager
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Servicio para manejar notificaciones push de Firebase
 */
class FCMService : FirebaseMessagingService() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object {
        private const val TAG = "FCMService"
        private var token: String? = null

        /**
         * Obtiene el token actual o lo solicita a Firebase
         */
        fun getToken(context: Context, callback: (String?) -> Unit) {
            if (token != null) {
                callback(token)
                return
            }

            // Intentar recuperar el token almacenado en preferencias
            val prefManager = PreferencesManager(context)

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    callback(null)
                    return@addOnCompleteListener
                }

                token = task.result

                // Guardar el token en preferencias
                CoroutineScope(Dispatchers.IO).launch {
                    prefManager.saveFCMToken(token ?: "")
                }

                callback(token)
            }
        }
    }

    override fun onNewToken(newToken: String) {
        Log.d(TAG, "Refreshed token: $newToken")
        token = newToken

        // Guardar el token en preferencias
        val prefManager = PreferencesManager(applicationContext)
        scope.launch {
            prefManager.saveFCMToken(newToken)
        }

        // Aquí añadirías la lógica para enviar el token al servidor
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Verificar si el mensaje contiene datos
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Verificar si el mensaje contiene una notificación
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            sendNotification(it.title ?: "Notificación", it.body ?: "")
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"] ?: return

        when (type) {
            "new_order" -> {
                val orderId = data["order_id"] ?: return
                // Aquí manejarías la lógica específica para nuevas órdenes
            }
            "order_assigned" -> {
                val orderId = data["order_id"] ?: return
                // Lógica para órdenes asignadas
            }
            "order_completed" -> {
                val orderId = data["order_id"] ?: return
                // Lógica para órdenes completadas
            }
        }
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Para Android Oreo y superior, es necesario crear un canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal de notificaciones",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }
}