package com.example.androidcookbook.ui.features.newsfeed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.NewsfeedRepository
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.usecase.DeletePostUseCase
import com.example.androidcookbook.domain.usecase.MakeToastUseCase
import com.example.androidcookbook.ui.common.state.ScreenUiState
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsfeedRepository: NewsfeedRepository,
    private val makeToastUseCase: MakeToastUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    var screenUiState = MutableStateFlow<ScreenUiState<List<Post>>>(ScreenUiState.Loading)
        private set
    var isRefreshing = MutableStateFlow(false)
        private set
    var posts = MutableStateFlow<List<Post>>(emptyList())
        private set

    private val newsfeedLimit = 10
    private var newsfeedOffset = newsfeedLimit

    init {
        refresh()
    }

    private suspend fun getNewsfeed() {
        val response = newsfeedRepository.getNewsfeed(newsfeedOffset)
        response.onSuccess {
            posts.update { data }
        }.onFailure {
            viewModelScope.launch {
                makeToastUseCase("Something went wrong")
            }
        }

    }

    fun refresh() {
        screenUiState.update { ScreenUiState.Loading }
        isRefreshing.update { true }
        viewModelScope.launch {
            getNewsfeed()
        }.invokeOnCompletion { throwable ->
            isRefreshing.update { false }
            if (throwable != null) {
                screenUiState.update { ScreenUiState.Failure(throwable.message ?: "Something went wrong") }
            } else {
                screenUiState.update { ScreenUiState.Success(posts.value) }
            }
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

    fun loadMore() {
        viewModelScope.launch {
            newsfeedOffset += newsfeedLimit
            val response = newsfeedRepository.getNewsfeed(newsfeedOffset)
            response.onSuccess {
                posts.update { data }
            }.onFailure {
                viewModelScope.launch {
                    makeToastUseCase("Something went wrong while fetching more posts")
                }
            }
        }
    }
}