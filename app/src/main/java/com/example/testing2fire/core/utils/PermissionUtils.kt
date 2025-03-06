package com.example.testing2fire.core.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Utilidades para manejar permisos
 */
object PermissionUtils {

    /**
     * Permisos de ubicación
     */
    val LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * Permisos de notificaciones (Android 13+)
     */
    val NOTIFICATION_PERMISSIONS = arrayOf(
        Manifest.permission.POST_NOTIFICATIONS
    )

    /**
     * Verifica si se tienen los permisos especificados
     */
    fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /**
     * Solicita permisos usando una launcher de resultado de actividad
     */
    fun requestPermissions(
        permissionLauncher: ActivityResultLauncher<Array<String>>,
        permissions: Array<String>
    ) {
        permissionLauncher.launch(permissions)
    }

    /**
     * Verifica si se debe mostrar la justificación del permiso
     */
    fun shouldShowRequestPermissionRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }
}