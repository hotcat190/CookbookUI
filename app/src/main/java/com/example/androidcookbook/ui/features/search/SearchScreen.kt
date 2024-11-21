package com.example.androidcookbook.ui.features.search

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    result: String,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { onBackButtonClick() }
    val viewModel: SearchViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState().value
    Text(text = result)
}

