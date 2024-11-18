package com.example.androidcookbook.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.domain.model.auth.RegisterRequest
import com.example.androidcookbook.domain.model.auth.SignInRequest
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
            val response = authRepository.login(SignInRequest(username, password))
            _uiState.value = when (response) {
                is ApiResponse.Success -> _uiState.value.copy(openDialog = true, dialogMessage = response.data.message, signInSuccess = true)
                is ApiResponse.Failure.Error -> _uiState.value.copy(openDialog = true, dialogMessage = response.payload.toString())
                is ApiResponse.Failure.Exception -> _uiState.value.copy(openDialog = true, dialogMessage = response.throwable.toString())
            }
        }
    }
}