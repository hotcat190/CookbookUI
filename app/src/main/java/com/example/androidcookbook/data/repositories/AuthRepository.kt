package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.AuthService
import com.example.androidcookbook.domain.model.auth.RegisterRequest
import com.example.androidcookbook.domain.model.auth.RegisterResponse
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun register(request: RegisterRequest): ApiResponse<RegisterResponse> {
        return authService.register(request)
    }

    suspend fun login(signInRequest: SignInRequest): ApiResponse<SignInResponse> {
        return authService.login(signInRequest) // Same here
    }
}

