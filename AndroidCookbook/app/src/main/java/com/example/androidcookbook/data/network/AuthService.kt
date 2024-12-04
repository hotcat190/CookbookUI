package com.example.androidcookbook.data.network

import com.example.androidcookbook.domain.model.auth.ForgotPasswordRequest
import com.example.androidcookbook.domain.model.auth.RegisterRequest
import com.example.androidcookbook.domain.model.auth.RegisterResponse
import com.example.androidcookbook.domain.model.auth.ResetPasswordRequest
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.example.androidcookbook.domain.network.SuccessMessageBody
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiResponse<RegisterResponse>

    @POST("auth/login")
    suspend fun login(@Body request: SignInRequest): ApiResponse<SignInResponse>

    @POST("auth/forgot-password")
    suspend fun sendForgotPasswordRequest(@Body request: ForgotPasswordRequest): ApiResponse<SuccessMessageBody>

    @POST("auth/reset-password")
    suspend fun sendResetPasswordRequest(@Body request: ResetPasswordRequest): ApiResponse<SuccessMessageBody>
}

