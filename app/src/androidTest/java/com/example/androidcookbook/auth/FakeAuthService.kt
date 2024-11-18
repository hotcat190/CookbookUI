package com.example.androidcookbook.auth

import com.example.androidcookbook.auth.TestAuthCredentials.password
import com.example.androidcookbook.auth.TestAuthCredentials.username
import com.example.androidcookbook.data.network.AuthService
import com.example.androidcookbook.domain.model.auth.RegisterRequest
import com.example.androidcookbook.domain.model.auth.RegisterResponse
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.skydoves.sandwich.ApiResponse
import java.net.SocketTimeoutException

class FakeAuthService : AuthService {
    override suspend fun register(request: RegisterRequest): ApiResponse<RegisterResponse> {
        return ApiResponse.Success(RegisterResponse(statusCode = 200, timestamp = "", path = "", message = "Register success", error = ""))
    }

    override suspend fun login(request: SignInRequest): ApiResponse<SignInResponse> {
        if (request.username == username && request.password == password)
            return ApiResponse.Success(
                SignInResponse(accessToken = "Token", message = "Sign in success")
            )
        return ApiResponse.Failure.Error("Wrong username or password")
    }
}

class TimeoutExceptionAuthService : AuthService {
    override suspend fun register(request: RegisterRequest): ApiResponse<RegisterResponse> {
        return ApiResponse.Failure.Exception(throwable = SocketTimeoutException("timeout"))
    }

    override suspend fun login(request: SignInRequest): ApiResponse<SignInResponse> {
        return ApiResponse.Failure.Exception(throwable = SocketTimeoutException("timeout"))
    }
}