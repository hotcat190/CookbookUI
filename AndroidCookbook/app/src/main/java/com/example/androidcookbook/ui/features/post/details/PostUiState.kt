package com.example.androidcookbook.ui.features.post.details

import com.example.androidcookbook.domain.model.post.Post

sealed class PostUiState {
    data class Success(val post: Post) : PostUiState()
    data class Error(val message: String) : PostUiState()
    data object Loading : PostUiState()
}