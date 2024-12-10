package com.example.androidcookbook.ui.features.userprofile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.UserRepository
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.GUEST_ID
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.domain.usecase.DeletePostUseCase
import com.example.androidcookbook.domain.usecase.MakeToastUseCase
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = UserProfileViewModel.UserProfileViewModelFactory::class)
class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val user: User,
    private val userRepository: UserRepository,
    private val deletePostUseCase: DeletePostUseCase,
    private val makeToastUseCase: MakeToastUseCase,
) : ViewModel() {

    @AssistedFactory
    interface UserProfileViewModelFactory {
        fun create(user: User): UserProfileViewModel
    }

    var isRefreshing: Boolean by mutableStateOf(false)
        private set

    var uiState: MutableStateFlow<UserProfileUiState> = MutableStateFlow(
        if (user.id == GUEST_ID) UserProfileUiState.Guest
        else UserProfileUiState.Success(user = user)
    )
        private set

    var userPostState: MutableStateFlow<UserPostState> = MutableStateFlow(
        if (user.id == GUEST_ID) UserPostState.Guest
        else UserPostState.Loading
    )



    init {
        viewModelScope.launch {
            getUser(userId = user.id)
            getUserPosts(userId = user.id)
        }
    }

    private suspend fun getUser(userId: Int) {
        if (user.id == GUEST_ID) {
            uiState.update { UserProfileUiState.Guest }
            return
        }
        uiState.update { UserProfileUiState.Loading }
        userRepository.getUserProfile(userId = userId)
            .onSuccess {
                uiState.update { UserProfileUiState.Success(user = data) }
                getUserPosts(userId)
            }
            .onFailure {
                uiState.update { UserProfileUiState.Failure }
            }
    }

    private fun getUserPosts(userId: Int) {
        if (user.id == GUEST_ID) {
            userPostState.update { UserPostState.Guest }
            return
        }
        userPostState.update { UserPostState.Loading }
        viewModelScope.launch {
            userRepository.getUserPosts(userId)
                .onSuccess {
                    userPostState.update { UserPostState.Success(data) }
                }
                .onFailure { userPostState.update { UserPostState.Failure } }
        }
    }

    fun refresh() {
        if (user.id == GUEST_ID) return
        viewModelScope.launch {
            isRefreshing = true
            getUser(userId = user.id)
            isRefreshing = false
        }
    }

    fun deletePost(post: Post) {
        viewModelScope.launch {
            deletePostUseCase(post).onSuccess {
                refresh()
            }.onFailure {
                viewModelScope.launch {
                    makeToastUseCase("Failed to delete post")
                }
            }
        }
    }
}