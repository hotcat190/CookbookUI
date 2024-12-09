package com.example.androidcookbook.ui.features.userprofile.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FollowButton(
    onFollowButtonClick: () -> Unit,
    isFollowing: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
    ) {
        if (isFollowing) {
            Button(
                onClick = onFollowButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text(
                    text = "Following",
                )
            }
        } else {
            Button(
                onClick = onFollowButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Text(
                    text = "Follow",
                )
            }
        }
    }
}