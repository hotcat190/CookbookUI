package com.example.androidcookbook.ui.screen

enum class CookbookScreen(
    val hasTopBar: Boolean = true,
    val hasBottomBar: Boolean = true
) {
    Category,
    AIChat,
    Newsfeed,
    UserProfile,
    Search(hasBottomBar = false),
    CreatePost(hasBottomBar = false),
    Login(hasTopBar = false, hasBottomBar = false),
}