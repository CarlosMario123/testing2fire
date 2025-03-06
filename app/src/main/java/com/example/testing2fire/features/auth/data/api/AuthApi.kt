package com.example.testing2fire.features.auth.data.api

import com.example.testing2fire.features.auth.data.model.LoginRequest
import com.example.testing2fire.features.auth.data.model.LoginResponse
import com.example.testing2fire.features.auth.data.model.RegisterRequest
import com.example.testing2fire.features.auth.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/couriers/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/couriers/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
}