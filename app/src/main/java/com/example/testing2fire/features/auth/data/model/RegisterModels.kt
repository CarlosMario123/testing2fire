package com.example.testing2fire.features.auth.data.model

data class RegisterRequest(
    val email: String,
    val name: String,
    val phone: String,
    val password: String,
    val fcm_token: String
)

data class RegisterResponse(
    val message: String,
    val token: String,
    val courier: CourierDTO
)