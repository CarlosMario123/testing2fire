package com.example.testing2fire.features.auth.presentation.register

data class RegisterState(
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val emailError: String? = null,
    val nameError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null
)