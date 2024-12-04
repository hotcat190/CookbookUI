package com.example.androidcookbook.domain.model.auth

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val password: String,
)

