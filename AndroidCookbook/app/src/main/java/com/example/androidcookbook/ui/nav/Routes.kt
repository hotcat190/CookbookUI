package com.example.androidcookbook.ui.nav

import kotlinx.serialization.Serializable

@Serializable
object Routes {

    @Serializable
    object Auth {
        @Serializable
        object Login

        @Serializable
        object Register

        @Serializable
        object ForgotPassword {
            @Serializable
            object Screen
            @Serializable
            object Otp
            @Serializable
            object Reset
        }
    }

    @Serializable
    object App {
        @Serializable
        object Category
        @Serializable
        object AIChat
        @Serializable
        object Newsfeed
        @Serializable
        data class UserProfile(val userId: Int)
    }

    @Serializable
    object Search
    @Serializable
    object CreatePost

    @Serializable
    object DialogDestination
}
