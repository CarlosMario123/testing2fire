package com.example.testing2fire.features.auth.data.model

data class LoginRequest(
    val email: String,
    val password: String,
    val fcm_token: String
)