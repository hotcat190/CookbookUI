package com.example.androidcookbook.ui.features.newsfeed

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.NewsfeedRepository
import com.example.androidcookbook.domain.model.post.Post
import com.skydoves.sandwich.ApiResponse.Companion.maps
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsfeedRepository: NewsfeedRepository
) : ViewModel() {

    var posts = MutableStateFlow<List<Post>>(emptyList())
        private set

    private val newsfeedLimit = 10

    init {
        refresh()
        Log.d("Newsfeed", posts.toString())
    }

    private fun getNewsfeed() {
        viewModelScope.launch {
            val response = newsfeedRepository.getNewsfeed(newsfeedLimit)
            response.onSuccess {
                posts.update { data }
            }.onFailure {
                //TODO
            }
            Log.d("Newsfeed", response.toString())
        }
    }

    fun refresh() {
        getNewsfeed()
    }


}