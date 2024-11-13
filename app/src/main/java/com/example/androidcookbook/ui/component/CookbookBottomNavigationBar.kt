package com.example.androidcookbook.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun CookbookBottomNavigationBar() {
    NavigationBar {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1F)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_category),
                    contentDescription = "Home"
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1F)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_message),
                    contentDescription = "Message"
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1F)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_create_post),
                    contentDescription = "Create post"
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.weight(1F)
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_user_profile),
                    contentDescription = "User profile"
                )
            }
        }
    }
}

@Preview
@Composable
fun NavBarPreview() {
    AndroidCookbookTheme {
        CookbookBottomNavigationBar()
    }
}

@Preview
@Composable
fun NavBarDarkPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        CookbookBottomNavigationBar()
    }
}