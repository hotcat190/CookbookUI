package com.example.androidcookbook.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.domain.model.auth.ForgotPasswordRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var email = MutableStateFlow("")
        private set
    var otpCode = MutableStateFlow("")
        private set
    var password = MutableStateFlow("")
        private set
    var retypePassword = MutableStateFlow("")
        private set

    fun updateEmail(email: String) = this.email.update { email }
    fun updateOtpCode(otpCode: String) = this.otpCode.update { otpCode }
    fun updatePassword(password: String) = this.password.update { password }
    fun updateRetypePassword(retypePassword: String) = this.retypePassword.update { retypePassword }

    fun submitEmail() {
        viewModelScope.launch {
            authRepository.sendForgotPasswordRequest(ForgotPasswordRequest(email.value))
        }
    }

    fun submitOtpRequest() {
        //TODO("Not yet implemented")
    }

    fun submitPasswordResetRequest() {
        viewModelScope.launch {
            //TODO password reset requests stuff

//            authRepository.sendPasswordResetRequest(
//                PasswordResetRequest(
//                    email = email.value,
//                    code = otpCode.value,
//                    password = password.value,
//                )
//            )
        }
    }
}

