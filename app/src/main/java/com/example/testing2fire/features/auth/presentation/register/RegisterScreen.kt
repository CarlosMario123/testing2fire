package com.example.testing2fire.features.auth.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.testing2fire.core.ui.components.AppTextField
import com.example.testing2fire.core.ui.components.PrimaryButton
import com.example.testing2fire.core.ui.components.SecondaryButton

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Registro de Repartidor",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            AppTextField(
                value = state.email,
                onValueChange = { viewModel.onEvent(RegisterEvent.EmailChanged(it)) },
                label = "Correo electrónico",
                error = state.emailError,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            AppTextField(
                value = state.name,
                onValueChange = { viewModel.onEvent(RegisterEvent.NameChanged(it)) },
                label = "Nombre completo",
                error = state.nameError,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            AppTextField(
                value = state.phone,
                onValueChange = { viewModel.onEvent(RegisterEvent.PhoneChanged(it)) },
                label = "Teléfono",
                error = state.phoneError,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                modifier = Modifier.padding(bottom = 5.dp)
            )

            AppTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(RegisterEvent.PasswordChanged(it)) },
                label = "Contraseña",
                error = state.passwordError,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isPassword = true,
                onImeAction = { viewModel.onEvent(RegisterEvent.RegisterClicked) },
                modifier = Modifier.padding(bottom = 5.dp)
            )

            PrimaryButton(
                text = "Registrarse",
                onClick = { viewModel.onEvent(RegisterEvent.RegisterClicked) },
                isLoading = state.isLoading,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            TextButton(
                onClick = { onNavigateToLogin() },
            ) {
                Text(text = "¿No tienes cuenta? Regístrate",
                    color = Color.White)
            }


            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}