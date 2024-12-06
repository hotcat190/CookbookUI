package com.example.androidcookbook.ui.features.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.domain.model.auth.ForgotPasswordRequest
import com.example.androidcookbook.domain.model.auth.ResetPasswordRequest
import com.example.androidcookbook.domain.network.ErrorBody
import com.example.androidcookbook.domain.network.SuccessMessageBody
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var email = MutableStateFlow("")
        private set
    var otpCode = MutableStateFlow("")
        private set
    var token = MutableStateFlow("")
        private set
    var password = MutableStateFlow("")
        private set
    var retypePassword = MutableStateFlow("")
        private set
    var openDialog = MutableStateFlow(false)
        private set
    var dialogMessage = MutableStateFlow("")
        private set
    var successSubmit = MutableStateFlow(false)
        private set

    fun updateEmail(email: String) = this.email.update { email }
    fun updateOtpCode(otpCode: String) = this.otpCode.update { otpCode }
    fun updatePassword(password: String) = this.password.update { password }
    fun updateRetypePassword(retypePassword: String) = this.retypePassword.update { retypePassword }
    fun updateOpenDialog(open: Boolean) = this.openDialog.update { open }
    fun updateDialogMessage(message: String) = this.dialogMessage.update { message }
    fun updateSuccessSubmit(success: Boolean) = this.successSubmit.update { success }

    fun submitEmail() {
        viewModelScope.launch {
            val response = authRepository.sendForgotPasswordRequest(ForgotPasswordRequest(email.value))
            response.onSuccess {
                updateOpenDialog(true)
                updateDialogMessage(data.message)
                updateSuccessSubmit(true)
            }.onErrorDeserialize<SuccessMessageBody, ErrorBody> {
                errorBody ->
                run {
                    updateOpenDialog(true)
                    updateDialogMessage(errorBody.message.joinToString(".\n\n") ?: "Error")
                }
            }.onException {
                when (throwable) {
                    is SocketTimeoutException ->
                        run {
                            updateOpenDialog(true)
                            updateDialogMessage("Request timed out.\nPlease try again.")
                        }
                }
            }
        }
    }

    fun submitOtpRequest() {
        //TODO("Not yet implemented")
    }

    fun submitPasswordResetRequest() {
        if (password.value != retypePassword.value) {
            updateOpenDialog(true)
            updateDialogMessage("Password and Retype Password must be the same")
            return
        }

        viewModelScope.launch {
            val response = authRepository.sendPasswordResetRequest(
                ResetPasswordRequest(
                    email = email.value,
                    token = token.value,
                    password = password.value,
                )
            )
            response.onSuccess {
                updateOpenDialog(true)
                updateDialogMessage(data.message)
                updateSuccessSubmit(true)
            }.onErrorDeserialize<SuccessMessageBody, ErrorBody> {
                errorBody ->
                run {
                    val rawResponse = errorBody.error
                    Log.e("API Error", "Raw Response: $rawResponse")
                    updateOpenDialog(true)
                    updateDialogMessage(errorBody.message.joinToString(".\n\n") ?: "Error")
                }
            }.onException {
                when (throwable) {
                    is SocketTimeoutException ->
                        run {
                            updateOpenDialog(true)
                            updateDialogMessage("Request timed out.\nPlease try again.")
                        }
                }
            }
        }
    }
}

