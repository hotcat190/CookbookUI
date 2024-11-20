package com.example.androidcookbook.ui

data class CookbookUiState (
    val showTopBar: Boolean = true,
    val showBottomBar: Boolean = true,
    val canNavigateBack: Boolean = false,
)