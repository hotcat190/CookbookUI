package com.example.androidcookbook.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SignUpComponents(
    email: String,
    onTypeEmail: (String) -> Unit,
    username: String,
    onTypeUsername: (String) -> Unit,
    password: String,
    onTypePassword: (String) -> Unit,
    repassword: String,
    onRetypePassword: (String) -> Unit,
    onSignUpClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        InputField(email, onTypeEmail, "Email", KeyboardType.Email)

        InputField(username, onTypeUsername, "Username", KeyboardType.Text)

        InputField(password, onTypePassword, "Password", KeyboardType.Password)

        InputField(repassword, onRetypePassword, "Repeat your password", KeyboardType.Password)

        SignButton(onClick = onSignUpClick, actionText = "Sign Up")
    }
}

