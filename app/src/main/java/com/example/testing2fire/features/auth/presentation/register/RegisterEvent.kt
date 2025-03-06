package com.example.testing2fire.features.auth.presentation.register

sealed class RegisterEvent {
    data class EmailChanged(val email: String) : RegisterEvent()
    data class NameChanged(val name: String) : RegisterEvent()
    data class PhoneChanged(val phone: String) : RegisterEvent()
    data class PasswordChanged(val password: String) : RegisterEvent()
    object RegisterClicked : RegisterEvent()
    object NavigateToLogin : RegisterEvent()
}