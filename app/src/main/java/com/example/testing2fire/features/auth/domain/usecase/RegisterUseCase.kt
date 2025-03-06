package com.example.testing2fire.features.auth.domain.usecase

import android.util.Patterns
import com.example.testing2fire.features.auth.domain.model.Courier
import com.example.testing2fire.features.auth.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        email: String,
        name: String,
        phone: String,
        password: String,
        fcmToken: String
    ): Result<Courier> {

        if (email.isBlank()) {
            return Result.failure(IllegalArgumentException("El correo electrónico es obligatorio"))
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("El correo electrónico no es válido"))
        }
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }
        if (phone.isBlank()) {
            return Result.failure(IllegalArgumentException("El teléfono es obligatorio"))
        }
        if (password.isBlank()) {
            return Result.failure(IllegalArgumentException("La contraseña es obligatoria"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener al menos 6 caracteres"))
        }
        if (fcmToken.isBlank()) {
            return Result.failure(IllegalArgumentException("El token FCM es obligatorio"))
        }

        return authRepository.register(email, name, phone, password, fcmToken)
    }
}