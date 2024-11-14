package com.example.androidcookbook.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun LoginScreen(
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF251404))
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            Image(
                painter = painterResource(R.drawable.cookbook_app_icon),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(150.dp)
            )
            Text(
                text = "CookBook",
                style = MaterialTheme.typography.titleLarge
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            LoginField(
                value = username,
                onValueChange = { value -> username = value },
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
            LoginField(
                value = password,
                onValueChange = { value -> password = value },
                modifier = Modifier
                    .padding(vertical = 16.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1F))
    }
}

@Composable
fun LoginField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
    )
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