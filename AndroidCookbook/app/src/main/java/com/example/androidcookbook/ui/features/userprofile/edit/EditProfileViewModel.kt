package com.example.androidcookbook.ui.features.userprofile.edit

import androidx.lifecycle.ViewModel
import com.example.androidcookbook.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

}