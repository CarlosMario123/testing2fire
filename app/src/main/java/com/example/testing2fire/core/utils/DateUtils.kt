package com.example.testing2fire.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Utilidades para el manejo de fechas
 */
object DateUtils {
    private val ISO_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    private val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val DATE_TIME_FORMAT = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    /**
     * Convierte una fecha ISO 8601 a formato legible
     */
    fun formatIsoDate(isoDateString: String?, pattern: String = "dd/MM/yyyy HH:mm"): String {
        if (isoDateString.isNullOrEmpty()) return ""

        return try {
            val date = ISO_FORMAT.parse(isoDateString) ?: return ""
            SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Formatea una fecha en un formato específico
     */
    fun formatDate(date: Date?, pattern: String): String {
        if (date == null) return ""

        return try {
            SimpleDateFormat(pattern, Locale.getDefault()).format(date)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Calcula la diferencia en minutos entre dos fechas
     */
    fun getMinutesBetween(startDate: Date?, endDate: Date?): Long {
        if (startDate == null || endDate == null) return 0

        val diffMillis = endDate.time - startDate.time
        return diffMillis / (1000 * 60)
    }
}