package com.example.testing2fire.features.auth.domain.usecase

import com.example.testing2fire.features.auth.domain.model.Courier
import com.example.testing2fire.features.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String, fcmToken: String): Result<Courier> {
        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo electrónico es obligatorio"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña es obligatoria"))
        }
        if (fcmToken.isBlank()) {
            return Result.failure(IllegalArgumentException("El token FCM es obligatorio"))
        }

        return authRepository.login(email, password, fcmToken)
    }
}