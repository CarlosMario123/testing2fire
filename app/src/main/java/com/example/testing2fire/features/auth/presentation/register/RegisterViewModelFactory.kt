package com.example.testing2fire.features.auth.presentation.register

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.courierapp.core.datastore.PreferencesManager
import com.example.testing2fire.core.factory.ViewModelFactory
import com.example.testing2fire.core.network.RetrofitConfig
import com.example.testing2fire.features.auth.data.api.AuthApi
import com.example.testing2fire.features.auth.data.repository.AuthRepositoryImpl
import com.example.testing2fire.features.auth.domain.usecase.RegisterUseCase

class RegisterViewModelFactory(private val context: Context) : ViewModelFactory<RegisterViewModel>() {

    override fun getViewModelClass(): Class<RegisterViewModel> {
        return RegisterViewModel::class.java
    }

    override fun createViewModel(): RegisterViewModel {
        // Crear todas las dependencias
        val preferencesManager = PreferencesManager(context)
        val authApi = RetrofitConfig.createApi(AuthApi::class.java)
        val authRepository = AuthRepositoryImpl(authApi, preferencesManager)
        val registerUseCase = RegisterUseCase(authRepository)

        // Crear el ViewModel con sus dependencias
        return RegisterViewModel(registerUseCase, preferencesManager, context)
    }
}