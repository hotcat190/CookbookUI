package com.example.androidcookbook.ui.features.auth.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.androidcookbook.ui.features.auth.AuthViewModel
import com.example.androidcookbook.ui.features.auth.components.MinimalDialog
import com.example.androidcookbook.ui.features.auth.components.SignLayout

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit
) {
    SignLayout {
        SignUpCompose(
            onSignInClick = onNavigateToSignIn,
            viewModel = authViewModel
        )
    }
    val uiState by authViewModel.uiState.collectAsState()
    if (uiState.openDialog) {
        MinimalDialog(
            dialogMessage = uiState.dialogMessage,
            onDismissRequest = {
                authViewModel.changeOpenDialog(false)
            }
        )
    }
}
