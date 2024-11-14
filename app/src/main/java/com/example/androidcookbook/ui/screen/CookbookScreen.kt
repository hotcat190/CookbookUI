package com.example.androidcookbook.ui.screen

enum class CookbookScreen(
    val hasTopBar: Boolean = true,
    val hasBottomBar: Boolean = true
) {
    Category,
    AIChat,
    Newsfeed,
    UserProfile,
    Search(hasBottomBar = false), // No top bar for Search screen
    CreatePost,
    Login(hasTopBar = false, hasBottomBar = false); // No top or bottom bar for Login screen
}