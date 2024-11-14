package com.example.androidcookbook.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun LoginScreen(
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {

    }
}

@Composable
@Preview
fun LoginScreenPreview() {
    AndroidCookbookTheme(darkTheme = false) {
        LoginScreen(
            {_,_ ->},
            {_,_ ->},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
@Preview
fun LoginScreenPreviewDarkTheme() {
    AndroidCookbookTheme(darkTheme = true) {
        LoginScreen(
            {_,_ ->},
            {_,_ ->},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}