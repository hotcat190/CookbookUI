package com.example.androidcookbook.ui.features.search

import androidx.activity.compose.BackHandler

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidcookbook.ui.common.appbars.SearchBar

@Composable
fun SearchScreen(
    providesCustomAppBar: (@Composable () -> Unit) -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val searchUiState = searchViewModel.uiState.collectAsState().value
    providesCustomAppBar {
        SearchBar(
            onSearch = { searchViewModel.search(it) },
            navigateBackAction = onBackButtonClick,
        )
    }
    BackHandler { onBackButtonClick() }
    Text(text = searchUiState.result)
}

