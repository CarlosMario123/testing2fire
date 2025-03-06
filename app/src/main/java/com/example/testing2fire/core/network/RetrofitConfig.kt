package com.example.testing2fire.core.network

import com.example.testing2fire.core.network.interceptors.AuthInterceptor
import com.example.testing2fire.core.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración central de Retrofit para toda la aplicación
 */
object RetrofitConfig {
    private val authInterceptor = AuthInterceptor()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Crea una implementación de una interfaz de API
     */
    fun <T> createApi(apiClass: Class<T>): T {
        return retrofit.create(apiClass)
    }

    /**
     * Actualiza el token de autenticación utilizado en las solicitudes
     */
    fun updateToken(token: String?) {
        authInterceptor.updateToken(token)
    }
}