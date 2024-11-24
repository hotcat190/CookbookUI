package com.example.androidcookbook.ui.features.userprofile

import com.example.androidcookbook.domain.model.user.User

sealed interface UserProfileUiState {
    data object Loading : UserProfileUiState
    data class Success(val user: User) : UserProfileUiState
    data object Failure: UserProfileUiState
}
