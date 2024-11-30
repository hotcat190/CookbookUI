package com.example.androidcookbook.ui.features.userprofile

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User

sealed interface UserProfileUiState {
    data object Loading : UserProfileUiState
    data class Success(val user: User) : UserProfileUiState
    data object Failure: UserProfileUiState
}

sealed interface UserPostState {
    data object Loading : UserPostState
    data class Success(val userPosts: List<Post>) : UserPostState
    data object Failure: UserPostState
}
