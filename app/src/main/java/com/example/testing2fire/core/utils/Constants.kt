package com.example.testing2fire.core.utils

/**
 * Constantes utilizadas en toda la aplicación
 */
object Constants {
    // API
    const val API_BASE_URL = "http://192.168.0.13:5000/api/"

    // Timeouts
    const val CONNECTION_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // Notification Channels
    const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default_channel"

    // Order Status
    const val ORDER_STATUS_PENDING = "pending"
    const val ORDER_STATUS_PROCESSING = "processing"
    const val ORDER_STATUS_COMPLETED = "completed"
}