package com.example.testing2fire.features.auth.domain.repository

import com.example.testing2fire.features.auth.domain.model.Courier


interface AuthRepository {
    suspend fun login(email: String, password: String, fcmToken: String): Result<Courier>
    suspend fun register(email: String, name: String, phone: String, password: String, fcmToken: String): Result<Courier>
}