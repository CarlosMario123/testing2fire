package com.example.testing2fire.services

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.testing2fire.MyApp
import com.example.testing2fire.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject

class FCMService:FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("dbug", "¡MENSAJE RECIBIDO! Data: ${message.data}")

        val title = message.data["title"] ?: "Nueva notificación"
        val body = message.data["body"] ?: "Tienes un mensaje nuevo"
        showNotification(title, body, message.data)

        // Procesar otros datos
        processCustomData(message.data)
    }

    private fun processCustomData(data: Map<String, String>) {
        try {
            // Ejemplo: Procesar un objeto usuario en formato JSON
            if (data.containsKey("usuario")) {
                val usuarioJson = data["usuario"]
                val usuarioObj = JSONObject(usuarioJson!!)

                val nombre = usuarioObj.optString("nombre", "")
                val nivel = usuarioObj.optInt("nivel", 0)
                val premium = usuarioObj.optBoolean("premium", false)

                Log.d("dbug", "Usuario - Nombre: $nombre, Nivel: $nivel, Premium: $premium")

            }

            // Ejemplo: Procesar un array en formato JSON
            if (data.containsKey("items")) {
                val itemsJson = data["items"]
                val itemsArray = JSONObject(itemsJson!!).getJSONArray("items")

                for (i in 0 until itemsArray.length()) {
                    val item = itemsArray.getJSONObject(i)
                    val id = item.optString("id", "")
                    val nombre = item.optString("nombre", "")

                    Log.d("dbug", "Item $i - ID: $id, Nombre: $nombre")
                }
            }

            // En el caso que se desee procesar una accion
            if (data.containsKey("accion")) {
                val accion = data["accion"]
                Log.d("dbug", "Acción a realizar: $accion")
                //Ejemplo hipotetico
                when (accion) {
                    "abrir_perfil" -> {

                    }
                    "actualizar" -> {
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("dbug", "Error al procesar datos: ${e.message}")
        }
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val notification = NotificationCompat.Builder(this, MyApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.baseline_notification)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}