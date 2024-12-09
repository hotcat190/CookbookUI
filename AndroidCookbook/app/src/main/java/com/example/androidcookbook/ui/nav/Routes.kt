package com.example.androidcookbook.ui.nav

import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
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
        object AIChef
        @Serializable
        object Newsfeed
        @Serializable
        data class UserProfile(val user: User)
        @Serializable
        data class PostDetails(val post: Post)
    }

    @Serializable
    object Search
    @Serializable
    object CreatePost
    @Serializable
    data class UpdatePost(val post: Post)

    @Serializable
    object DialogDestination
}
