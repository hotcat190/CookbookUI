package com.example.androidcookbook.ui.features.auth.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun SignInComponents(
    username: String,
    onTypeUsername: (String) -> Unit,
    password: String,
    onTypePassword: (String) -> Unit,
    onSignInClick: (String, String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        InputField(username, onTypeUsername, "Username", KeyboardType.Text)

        InputField(password, onTypePassword, "Password", KeyboardType.Password)

        SignButton(
            onClick = { onSignInClick(username, password) },
            actionText = "Sign In"
        )

    }
}

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

const val USERNAME_TEXT_FIELD_TEST_TAG = "username"
const val PASSWORD_TEXT_FIELD_TEST_TAG = "password"

@Composable
fun MinimalDialog(
    onDismissRequest: () -> Unit,
    dialogMessage: String
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = dialogMessage,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
        }
    }
}