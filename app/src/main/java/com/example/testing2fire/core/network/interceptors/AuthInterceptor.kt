package com.example.testing2fire.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.atomic.AtomicReference

/**
 * Interceptor que añade el token de autenticación a las peticiones
 */
class AuthInterceptor : Interceptor {
    private val token = AtomicReference<String?>(null)

    fun updateToken(newToken: String?) {
        token.set(newToken)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val currentToken = token.get() ?: return chain.proceed(originalRequest)

        // Añadir el token de autenticación al header
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $currentToken")
            .build()

        return chain.proceed(newRequest)
    }
}