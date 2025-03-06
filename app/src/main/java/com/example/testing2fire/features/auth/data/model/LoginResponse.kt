package com.example.testing2fire.features.auth.data.model

data class LoginResponse(
    val message: String,
    val token: String,
    val courier: CourierDTO
)

data class CourierDTO(
    val _id: String,
    val email: String,
    val name: String,
    val phone: String
)