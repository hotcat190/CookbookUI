package com.example.androidcookbook.ui.features.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcookbook.data.repositories.AllSearcherRepository
import com.example.androidcookbook.data.repositories.SearchRepository
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.domain.model.recipe.RecipeList
import com.example.androidcookbook.domain.model.search.SearchAll
import com.example.androidcookbook.domain.model.search.SearchPost
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val allSearcherRepository: AllSearcherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(searchQuery: String) {
        viewModelScope.launch {
            val response: ApiResponse<RecipeList?> = searchRepository.search(searchQuery)
            response.onSuccess {
                val meals = data?.meals
                if (meals != null) {
                    _uiState.update {
                        it.copy(result = meals.toString(), resultList = meals, fail = false, searchQuery = searchQuery)
                    }
                    ChangeScreenState(SearchScreenState.Food)
                } else {
                    _uiState.update { it.copy(result = "No meals found", fail = true) }
                }
            }.onFailure {
                _uiState.update { it.copy(result = "Failed to fetch data", fail = true) }
            }
        }
    }

    fun searchAll(query: String) {
        viewModelScope.launch {
            val response: ApiResponse<SearchAll> = allSearcherRepository.searchAll(query)
            _uiState.update {
                it.copy(
                    postTabState = SearchTabUiState(),
                    userTabState = SearchTabUiState(),
                    foodTabState = SearchTabUiState()
                )
            }
            response.onSuccess {
                val result = data
                if (result != null) {
                    _uiState.update {
                        it.copy(result = data.toString(), searchALlResults = result, fail = false, searchQuery = query)
                    }
                    ChangeScreenState(SearchScreenState.Food)
                } else {
                    _uiState.update { it.copy(result = "Found nothing", fail = true) }
                }
            }.onFailure {
                _uiState.update { it.copy(result = "Failed to fetch data", fail = true) }
            }
        }
    }

    fun searchPost() {
        viewModelScope.launch {
            val response: ApiResponse<SearchPost> = allSearcherRepository
                .searchPosts(
                    _uiState.value.searchQuery,
                    _uiState.value.postTabState.currentPage + 1
                )
            response.onSuccess {
                if (data.posts != null) {
                    _uiState.update {
                        it.copy(
                            postTabState = SearchTabUiState(
                                currentPage = it.postTabState.currentPage + 1,
                                result = it.postTabState.result + data.posts,
                                isFail = false,
                                messageStr = "Search successfully"
                            )
                        )
                    }

                } else {
                    _uiState.update {
                        it.copy(
                            postTabState = SearchTabUiState(
                                isFail = true,
                                messageStr = "Couldn't find any posts"
                            )
                        )
                    }
                }
            }.onFailure {
                _uiState.update {
                    it.copy(
                        postTabState = SearchTabUiState(
                            isFail = true,
                            messageStr = "Search failed"
                        )
                    )
                }
                Log.d("Search Post", "searchPost: " )

            }
        }
    }

    fun ChangeScreenState(screen: SearchScreenState) {
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }

    fun ChangeCurrentPost(post: Post) {
        _uiState.update {
            it.copy(
                currentPost = post
            )
        }
    }


}