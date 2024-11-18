package com.example.androidcookbook.ui.features.auth.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidcookbook.ui.features.auth.components.MinimalDialog
import com.example.androidcookbook.ui.features.auth.components.SignLayout

@Composable
fun LoginScreen(
    onForgotPasswordClick: () -> Unit,
    onSignInClick: (String, String) -> Unit,
    onNavigateToSignUp: () -> Unit,
    isDialogOpen: Boolean = false,
    dialogMessage: String = "",
    onDialogDismiss: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    SignLayout {
        SignInCompose(
            onSignInClick = onSignInClick,
            onForgotPasswordClick = onForgotPasswordClick,
            onSignUpClick = onNavigateToSignUp,
        )
    }
    if (isDialogOpen) {
        MinimalDialog(
            dialogMessage = dialogMessage,
            onDismissRequest = onDialogDismiss
        )
    }
}

@Preview
@Composable
fun LoginPreview() {
    LoginScreen(
        {},{_,_->},{}
    )
}