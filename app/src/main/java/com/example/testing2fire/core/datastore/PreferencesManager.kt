package com.example.courierapp.core.datastore

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestor de preferencias que utiliza SharedPreferences para almacenar datos
 */
class PreferencesManager(context: Context) {
    companion object {
        private const val PREFS_NAME = "courier_preferences"

        // Keys para las preferencias
        const val AUTH_TOKEN = "auth_token"
        const val FCM_TOKEN = "fcm_token"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // StateFlows para observar cambios
    private val authTokenFlow = MutableStateFlow<String?>(sharedPreferences.getString(AUTH_TOKEN, null))
    private val fcmTokenFlow = MutableStateFlow<String?>(sharedPreferences.getString(FCM_TOKEN, null))

    /**
     * Guarda un valor en las preferencias
     */
    suspend fun savePreference(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()

        // Actualizar el Flow correspondiente
        when (key) {
            AUTH_TOKEN -> authTokenFlow.value = value
            FCM_TOKEN -> fcmTokenFlow.value = value
        }
    }

    /**
     * Obtiene un valor de las preferencias
     */
    fun getPreference(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    /**
     * Obtiene un valor de las preferencias como Flow
     */
    fun getPreferenceFlow(key: String): Flow<String?> {
        return when (key) {
            AUTH_TOKEN -> authTokenFlow.asStateFlow()
            FCM_TOKEN -> fcmTokenFlow.asStateFlow()
            else -> {
                // Para otros valores, creamos un Flow estático
                MutableStateFlow(sharedPreferences.getString(key, null)).asStateFlow()
            }
        }
    }

    /**
     * Borra todas las preferencias (útil para logout)
     */
    suspend fun clearAllPreferences() {
        sharedPreferences.edit().clear().apply()

        // Actualizar todos los Flows
        authTokenFlow.value = null
        fcmTokenFlow.value = null
    }

    /**
     * Métodos específicos para manejar el token de autenticación
     */
    suspend fun saveAuthToken(token: String) {
        savePreference(AUTH_TOKEN, token)
    }

    fun getAuthToken(): String? {
        return getPreference(AUTH_TOKEN)
    }

    fun getAuthTokenFlow(): Flow<String?> {
        return authTokenFlow.asStateFlow()
    }

    /**
     * Métodos específicos para manejar el token de FCM
     */
    suspend fun saveFCMToken(token: String) {
        savePreference(FCM_TOKEN, token)
    }

    fun getFCMToken(): String? {
        return getPreference(FCM_TOKEN)
    }

    fun getFCMTokenFlow(): Flow<String?> {
        return fcmTokenFlow.asStateFlow()
    }
}