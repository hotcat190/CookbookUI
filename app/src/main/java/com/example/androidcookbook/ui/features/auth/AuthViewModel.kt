package com.example.androidcookbook.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.domain.model.auth.RegisterRequest
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.example.androidcookbook.domain.model.auth.SignInResponse
import com.example.androidcookbook.domain.network.ErrorMessage
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.serialization.onErrorDeserialize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun changeOpenDialog(open: Boolean) {
        _uiState.update {
            it.copy(
                openDialog = open
            )
        }
    }

    fun changeDialogMessage(message: String) {
        _uiState.update {
            it.copy(
                dialogMessage = message
            )
        }
    }

    private fun signInSuccess() {
        _uiState.update {
            it.copy(
                signInSuccess = true
            )
        }
    }

    fun signUp(req: RegisterRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.register(req)
            _uiState.value = when (response) {
                is ApiResponse.Success -> _uiState.value.copy(openDialog = true, dialogMessage = response.data.message)
                is ApiResponse.Failure.Error -> _uiState.value.copy(openDialog = true, dialogMessage = response.message())
                is ApiResponse.Failure.Exception -> _uiState.value.copy(openDialog = true, dialogMessage = response.message())
            }
        }
    }

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            // Send request and receive the response
            val response = authRepository.login(SignInRequest(username, password))
            response.onSuccess {
                _uiState.update { it.copy(openDialog = true, dialogMessage = data.message, signInSuccess = true) }
            }.onErrorDeserialize<SignInResponse, ErrorMessage> { errorMessage ->
                _uiState.update { it.copy(openDialog = true, dialogMessage = errorMessage.message.joinToString("\n")) }
            }.onException {
                when (throwable) {
                    is SocketTimeoutException -> _uiState.update { it.copy(openDialog = true, dialogMessage = "Request timed out.\n Please try again.") }
                }
            }
        }
    }
}