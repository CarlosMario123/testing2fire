package com.example.testing2fire.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.testing2fire.features.auth.presentation.login.LoginScreen
import com.example.testing2fire.features.auth.presentation.register.RegisterScreen
import com.example.testing2fire.features.auth.presentation.register.RegisterViewModel
import com.example.testing2fire.features.auth.presentation.register.RegisterViewModelFactory

fun NavGraphBuilder.authRoutes(navController: NavHostController) {
    composable(route = Screen.Login.route) {
        LoginScreen(
            onLoginSuccess = {
                navController.navigate(Screen.PendingOrders.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onNavigateToRegister = { navController.navigate(Screen.Register.route) }
        )
    }

    composable(route = Screen.Register.route) {
        val context = LocalContext.current
        val registerViewModel: RegisterViewModel = viewModel(
            factory = RegisterViewModelFactory(context)
        )
        RegisterScreen(
            viewModel = registerViewModel,
            onNavigateToLogin = { navController.navigateUp() },
            onRegisterSuccess = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            }
        )
    }
}
