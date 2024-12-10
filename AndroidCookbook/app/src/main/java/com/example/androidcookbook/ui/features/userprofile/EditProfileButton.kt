package com.example.androidcookbook.ui.features.userprofile

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun EditProfileButton(
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onEditProfileClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {
        Text(
            text = "Edit profile",
        )
    }
}

@Composable
@Preview
private fun EditProfileButtonPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        UserProfileHeader(
            bannerPath = null,
            avatarPath = null,
            headerButton = {
                EditProfileButton(onEditProfileClick = {})
            }
        )
    }
}