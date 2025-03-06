package com.example.testing2fire.features.auth.data.repository

import com.example.courierapp.core.datastore.PreferencesManager
import com.example.testing2fire.features.auth.data.model.LoginRequest
import com.example.testing2fire.features.auth.data.model.RegisterRequest
import com.example.testing2fire.features.auth.data.api.AuthApi
import com.example.testing2fire.features.auth.data.mapper.AuthMapper.toDomain
import com.example.testing2fire.features.auth.domain.model.Courier
import com.example.testing2fire.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String, fcmToken: String): Result<Courier> = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(email, password, fcmToken)
            val response = authApi.login(request)

            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse != null) {
                    preferencesManager.saveAuthToken(loginResponse.token)
                    preferencesManager.savePreference(PreferencesManager.USER_ID, loginResponse.courier._id)
                    preferencesManager.savePreference(PreferencesManager.USER_EMAIL, loginResponse.courier.email)
                    preferencesManager.savePreference(PreferencesManager.USER_NAME, loginResponse.courier.name)

                    Result.success(loginResponse.courier.toDomain())
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, name: String, phone: String, password: String, fcmToken: String): Result<Courier> = withContext(Dispatchers.IO) {
        try {
            val request = RegisterRequest(email, name, phone, password, fcmToken)
            val response = authApi.register(request)

            if (response.isSuccessful) {
                val registerResponse = response.body()
                if (registerResponse != null) {
                    preferencesManager.saveAuthToken(registerResponse.token)
                    preferencesManager.savePreference(PreferencesManager.USER_ID, registerResponse.courier._id)
                    preferencesManager.savePreference(PreferencesManager.USER_EMAIL, registerResponse.courier.email)
                    preferencesManager.savePreference(PreferencesManager.USER_NAME, registerResponse.courier.name)

                    Result.success(registerResponse.courier.toDomain())
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}