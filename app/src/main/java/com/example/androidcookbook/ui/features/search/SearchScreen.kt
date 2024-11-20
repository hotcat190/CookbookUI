package com.example.androidcookbook.ui.features.search

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcookbook.ui.common.appbars.SearchBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    result: String,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: SearchViewModel = viewModel()
    Scaffold(
        topBar = {
            SearchBar(
                onValueChange = { updatedSearchQuery ->
                    viewModel.updateSearchQuery(updatedSearchQuery)
                },
                navigateBackAction = { },
                searchQuery = ""
            )
        }
    ) {

    }
    BackHandler { onBackButtonClick() }
    Text(text = result)
}

