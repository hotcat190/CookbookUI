package com.example.androidcookbook.ui.features.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.data.mocks.SampleNotifications
import com.example.androidcookbook.domain.model.notification.Notification
import com.example.androidcookbook.domain.model.notification.NotificationType
import com.example.androidcookbook.ui.common.text.TextWithBoldStrings
import com.example.androidcookbook.ui.components.post.SmallAvatar
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

val NotificationIconMap = mapOf(
    NotificationType.NEW_FOLLOWER to Icons.Default.PersonAddAlt1,
    NotificationType.NEW_POST_LIKE to Icons.Default.Favorite,
    NotificationType.NEW_COMMENT_LIKE to Icons.Default.Favorite,
    NotificationType.NEW_POST_COMMENT to Icons.AutoMirrored.Filled.Comment,
)

val NotificationColorMap = mapOf(
    NotificationType.NEW_FOLLOWER to Color.Blue,
    NotificationType.NEW_POST_LIKE to Color.Red,
    NotificationType.NEW_COMMENT_LIKE to Color.Red,
    NotificationType.NEW_POST_COMMENT to Color.Blue,
)


@Composable
fun NotificationItem(
    notification: Notification,
    onClick: (Notification) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick(notification) }
    ) {
        Icon(
            imageVector = NotificationIconMap[notification.type] ?: Icons.Default.Notifications,
            contentDescription = "Notification Icon",
            tint = NotificationColorMap[notification.type] ?: Color.Black,
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(
                modifier.padding(bottom = 8.dp)
            ) {
                SmallAvatar(notification.imageURL)
            }

            TextWithBoldStrings(notification.message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun NotificationItemPreview() {
    AndroidCookbookTheme {
        NotificationItem(
            notification = SampleNotifications.notifications[0],
            onClick = {}
        )
    }
}