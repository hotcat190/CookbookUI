package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserProfile(userId: Int) = userService.getUserProfile(userId)

    suspend fun getUserPosts(userId: Int) = userService.getUserPosts(userId)

    suspend fun getUserFollowers(userId: Int) = userService.getUserFollowers(userId)

    suspend fun getUserFollowing(userId: Int) = userService.getUserFollowing(userId)

    suspend fun followUser(userId: Int) = userService.followUser(userId)

    suspend fun unfollowUser(userId: Int) = userService.unfollowUser(userId)
}
