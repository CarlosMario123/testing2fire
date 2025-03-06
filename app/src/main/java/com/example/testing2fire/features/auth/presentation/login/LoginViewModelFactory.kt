package com.example.testing2fire.features.auth.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.courierapp.core.datastore.PreferencesManager
import com.example.testing2fire.core.factory.ViewModelFactory
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.features.auth.data.api.AuthApi
import com.example.testing2fire.features.auth.domain.usecase.LoginUseCase
import com.example.testing2fire.features.auth.data.repository.AuthRepositoryImpl


class LoginViewModelFactory(private val context: Context) : ViewModelFactory<LoginViewModel>() {

    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }

    override fun createViewModel(): LoginViewModel {
        // Crear todas las dependencias
        val preferencesManager = PreferencesManager(context)
        val authApi = RetrofitConfig.createApi(AuthApi::class.java)
        val authRepository = AuthRepositoryImpl(authApi, preferencesManager)
        val loginUseCase = LoginUseCase(authRepository)

        // Crear el ViewModel con sus dependencias
        return LoginViewModel(loginUseCase, preferencesManager, context)
    }
}