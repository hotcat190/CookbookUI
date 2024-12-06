package com.example.androidcookbook.ui.common.appbars

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    onSearch: (String) -> Unit,
    navigateBackAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery: String by remember { mutableStateOf("") }
    Log.d("SearchBar", searchQuery)

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect (Unit) {
        focusRequester.requestFocus()
    }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Box(modifier = Modifier.height(48.dp)) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    value = searchQuery,
                    onValueChange = {searchQuery = it},
                    placeholder = { Text("Search..", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary) },
                    shape = RoundedCornerShape(16.dp),
                    textStyle = MaterialTheme.typography.labelMedium,
                    colors = TextFieldDefaults.colors().copy(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.secondary,
                        focusedTextColor = MaterialTheme.colorScheme.secondary,
                        unfocusedTextColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = Color.Transparent.copy(alpha = .15f),
                        unfocusedContainerColor = Color.Transparent.copy(alpha = .15f),
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onSearch(searchQuery) }
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = navigateBackAction) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back Button",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        },
    )
}

@Preview
@Composable
fun SearchPreview() {
    AndroidCookbookTheme(darkTheme = false) {
        SearchBar(
            onSearch = {_->},
            navigateBackAction = {}
        )
    }
}