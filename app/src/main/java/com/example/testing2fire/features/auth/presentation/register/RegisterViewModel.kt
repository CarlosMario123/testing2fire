package com.example.testing2fire.features.auth.presentation.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.courierapp.core.datastore.PreferencesManager
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.core.services.FCMService
import com.example.testing2fire.features.auth.domain.usecase.RegisterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val preferencesManager: PreferencesManager,
    private val context: android.content.Context
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private var fcmToken: String? = null

    init {
        // Obtener token FCM al inicializar
        FCMService.getToken(context) { token ->
            fcmToken = token
        }
    }

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EmailChanged -> {
                _state.update { it.copy(
                    email = event.email,
                    emailError = null,
                    errorMessage = null
                ) }
            }
            is RegisterEvent.NameChanged -> {
                _state.update { it.copy(
                    name = event.name,
                    nameError = null,
                    errorMessage = null
                ) }
            }
            is RegisterEvent.PhoneChanged -> {
                _state.update { it.copy(
                    phone = event.phone,
                    phoneError = null,
                    errorMessage = null
                ) }
            }
            is RegisterEvent.PasswordChanged -> {
                _state.update { it.copy(
                    password = event.password,
                    passwordError = null,
                    errorMessage = null
                ) }
            }
            is RegisterEvent.RegisterClicked -> {
                validateAndRegister()
            }
            is RegisterEvent.NavigateToLogin -> {
                // Esto se manejará en la UI
            }
        }
    }

    private fun validateAndRegister() {
        val emailResult = validateEmail(_state.value.email)
        val nameResult = validateName(_state.value.name)
        val phoneResult = validatePhone(_state.value.phone)
        val passwordResult = validatePassword(_state.value.password)

        val hasError = listOf(emailResult, nameResult, phoneResult, passwordResult).any { !it.second }

        if (hasError) {
            _state.update { state ->
                state.copy(
                    emailError = if (emailResult.second) null else emailResult.first,
                    nameError = if (nameResult.second) null else nameResult.first,
                    phoneError = if (phoneResult.second) null else phoneResult.first,
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

        register()
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

    private fun validateName(name: String): Pair<String?, Boolean> {
        if (name.isBlank()) {
            return Pair("El nombre es obligatorio", false)
        }
        if (name.length < 3) {
            return Pair("El nombre debe tener al menos 3 caracteres", false)
        }
        return Pair(null, true)
    }

    private fun validatePhone(phone: String): Pair<String?, Boolean> {
        if (phone.isBlank()) {
            return Pair("El teléfono es obligatorio", false)
        }
        if (!phone.matches(Regex("^[0-9]{10}$"))) {
            return Pair("El teléfono debe tener 10 dígitos", false)
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

    private fun register() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val result = registerUseCase(
                    email = _state.value.email,
                    name = _state.value.name,
                    phone = _state.value.phone,
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
                            else -> exception.message ?: "Error desconocido en el registro"
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