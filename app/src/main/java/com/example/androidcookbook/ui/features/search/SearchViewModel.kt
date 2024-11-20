package com.example.androidcookbook.ui.features.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun updateSearchQuery(updatedSearchQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = updatedSearchQuery
            )
        }
    }
}