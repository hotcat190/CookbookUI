package com.example.androidcookbook.ui.features.newsfeed

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcookbook.data.repositories.NewsfeedRepository
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.usecase.DeletePostUseCase
import com.example.androidcookbook.domain.usecase.MakeToastUseCase
import com.skydoves.sandwich.ApiResponse.Companion.maps
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsfeedRepository: NewsfeedRepository,
    private val makeToastUseCase: MakeToastUseCase,
    private val deletePostUseCase: DeletePostUseCase,
) : ViewModel() {

    var posts = MutableStateFlow<List<Post>>(emptyList())
        private set

    private val newsfeedLimit = 10

    init {
        refresh()
    }

    private fun getNewsfeed() {
        viewModelScope.launch {
            val response = newsfeedRepository.getNewsfeed(newsfeedLimit)
            response.onSuccess {
                posts.update { data }
            }.onFailure {
                viewModelScope.launch {
                    makeToastUseCase("Something went wrong")
                }
            }
        }
    }

    fun refresh() {
        getNewsfeed()
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