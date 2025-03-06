package com.example.testing2fire.features.auth.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testing2fire.core.ui.components.AppTextField
import com.example.testing2fire.core.ui.components.PrimaryButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(LocalContext.current)
    )
) {
    val state by viewModel.state.collectAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = true) {
        viewModel.state.collectLatest { state ->
            if (state.isSuccess) {
                onLoginSuccess()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp),
                color = Color.White

            )

            // Campo de correo electrónico
            AppTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = "Correo electrónico",
                error = state.emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Campo de contraseña
            AppTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = "Contraseña",
                error = state.passwordError,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                onImeAction = {
                    focusManager.clearFocus()
                    viewModel.onEvent(LoginEvent.LoginClicked)
                },
                modifier = Modifier.padding(bottom = 24.dp)

            )

            // Botón de inicio de sesión
            PrimaryButton(
                text = "Iniciar Sesión",

                onClick = { viewModel.onEvent(LoginEvent.LoginClicked) },
                isLoading = state.isLoading,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Texto para navegar a registro
            TextButton(
                onClick = { onNavigateToRegister() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "¿No tienes cuenta? Regístrate",
                    color = Color.White)
            }

            // Mostrar mensaje de error si existe
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        // Mostrar indicador de carga si está cargando
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}