package com.example.androidcookbook.data.repositories

import com.example.androidcookbook.data.network.UserService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUserProfile(userId: Int) = userService.getUserProfile(userId)
}
