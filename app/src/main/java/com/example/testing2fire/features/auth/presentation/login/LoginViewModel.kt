package com.example.testing2fire.features.auth.presentation.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courierapp.core.datastore.PreferencesManager
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.core.services.FCMService

import com.example.testing2fire.features.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val preferencesManager: PreferencesManager,
    private val context: android.content.Context
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private var fcmToken: String? = null

    init {
        // Obtener token FCM al inicializar
        FCMService.getToken(context) { token ->
            fcmToken = token
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _state.update { it.copy(
                    email = event.email,
                    emailError = null,
                    errorMessage = null
                ) }
            }
            is LoginEvent.PasswordChanged -> {
                _state.update { it.copy(
                    password = event.password,
                    passwordError = null,
                    errorMessage = null
                ) }
            }
            is LoginEvent.LoginClicked -> {
                validateAndLogin()
            }
            is LoginEvent.NavigateToRegister -> {
                // Esto se manejará en la UI
            }
        }
    }

    private fun validateAndLogin() {
        val emailResult = validateEmail(_state.value.email)
        val passwordResult = validatePassword(_state.value.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.second }

        if (hasError) {
            _state.update { state ->
                state.copy(
                    emailError = if (emailResult.second) null else emailResult.first,
                    passwordError = if (passwordResult.second) null else passwordResult.first
                )
            }
            return
        }

        // Validar token FCM
        if (fcmToken.isNullOrEmpty()) {
            _state.update { it.copy(
                errorMessage = "No se pudo obtener el token FCM. Intente nuevamente."
            ) }
            return
        }

        login()
    }

    private fun validateEmail(email: String): Pair<String?, Boolean> {
        if (email.isBlank()) {
            return Pair("El correo electrónico es obligatorio", false)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Pair("El correo electrónico no es válido", false)
        }
        return Pair(null, true)
    }

    private fun validatePassword(password: String): Pair<String?, Boolean> {
        if (password.isBlank()) {
            return Pair("La contraseña es obligatoria", false)
        }
        if (password.length < 6) {
            return Pair("La contraseña debe tener al menos 6 caracteres", false)
        }
        return Pair(null, true)
    }

    private fun login() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val result = loginUseCase(
                    email = _state.value.email,
                    password = _state.value.password,
                    fcmToken = fcmToken ?: ""
                )

                result.fold(
                    onSuccess = { courier ->
                        RetrofitConfig.updateToken(preferencesManager.getAuthToken())
                        _state.update { it.copy(isLoading = false, isSuccess = true) }
                    },
                    onFailure = { exception ->
                        val errorMessage = when(exception) {
                            is IOException -> "Error de conexión. Verifique su conexión a Internet."
                            else -> exception.message ?: "Error desconocido en el inicio de sesión"
                        }
                        _state.update { it.copy(isLoading = false, errorMessage = errorMessage) }
                    }
                )
            } catch (e: Exception) {
                _state.update { it.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Error desconocido"
                ) }
            }
        }
    }
}