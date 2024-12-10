package com.example.androidcookbook.ui.components.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun PostHeader(
    author: User,
    createdAt: String?,
    onEditPost: () -> Unit,
    onDeletePost: () -> Unit,
    onUserClick: (User) -> Unit,
    showOptionsButton: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        SmallAvatar(
            author = author,
            onUserClick = onUserClick,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.width(8.dp)) // Spacing between icon and username
        Column {
            val roundedCornerShape = RoundedCornerShape(45)
            val leftPad = 4.dp
            Box (
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                        shape = roundedCornerShape
                    )
                    .clip(roundedCornerShape)
                    .clickable {
                        onUserClick(author)
                    }
            ) {
                Text(
                    text = author.name,
                    fontSize = 19.sp,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = leftPad)
                )
            }
            Text(
                text = createdAt ?: "",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.padding(start = leftPad)
            )
        }
        Spacer(Modifier.weight(1F))
        if (showOptionsButton) {
            IconButton(
                onClick = {
                    expanded = true
                },
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Open Options",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = {
                            onEditPost()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDeletePost()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PostHeaderPreview() {
    AndroidCookbookTheme {
        Column(
            modifier = Modifier
                .height(250.dp)
        ) {
            PostHeader(
                author = User(),
                createdAt = "2024-01-01",
                showOptionsButton = true,
                onDeletePost = {},
                onEditPost = {},
                onUserClick = {},
            )
        }
    }
}