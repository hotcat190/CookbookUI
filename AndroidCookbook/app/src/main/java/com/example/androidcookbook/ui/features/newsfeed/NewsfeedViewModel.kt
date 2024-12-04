package com.example.androidcookbook.ui.features.newsfeed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.NewsfeedRepository
import com.example.androidcookbook.domain.model.post.Post
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsfeedViewModel @Inject constructor(
    private val newsfeedRepository: NewsfeedRepository
) : ViewModel() {

    var posts: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
        private set

    init {
        getNewsfeed()
        Log.d("Newsfeed", posts.toString())
    }

    private fun getNewsfeed() {
        viewModelScope.launch {
            newsfeedRepository.getNewsfeed(1)
                .onSuccess {
                    posts.update { data }
                }
                .onFailure {
                    //TODO
                }
        }
    }


}