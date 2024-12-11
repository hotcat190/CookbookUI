package com.example.androidcookbook.ui.features.notification

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.data.mocks.SampleNotifications
import com.example.androidcookbook.domain.model.notification.Notification
import com.example.androidcookbook.ui.common.appbars.AppBarTheme
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun NotificationScreen(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit,
    onBackButtonClick: () -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            AppBarTheme {
                NotificationScreenTopBar(onBackButtonClick, onClearAllClick)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(notifications) { notification ->
                NotificationItem(notification, onNotificationClick,
                )
            }
            item { // Empty state if no notifications
                if (notifications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No notifications yet")
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun NotificationScreenDarkPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        NotificationScreen(
            notifications = SampleNotifications.notifications,
            onNotificationClick = {},
            onBackButtonClick = {},
            onClearAllClick = {}
        )
    }
}

@Composable
@Preview
private fun NotificationScreenPreview() {
    AndroidCookbookTheme {
        NotificationScreen(
            notifications = SampleNotifications.notifications,
            onNotificationClick = {},
            onBackButtonClick = {},
            onClearAllClick = {}
        )
    }
}