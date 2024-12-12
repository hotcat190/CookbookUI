package com.example.androidcookbook.ui.nav.dest

import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.androidcookbook.domain.model.notification.NotificationType
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.common.appbars.AppBarTheme
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.common.screens.FailureScreen
import com.example.androidcookbook.ui.common.screens.GuestLoginScreen
import com.example.androidcookbook.ui.common.screens.LoadingScreen
import com.example.androidcookbook.ui.common.state.ScreenUiState
import com.example.androidcookbook.ui.features.notification.NotificationScreen
import com.example.androidcookbook.ui.features.notification.NotificationScreenTopBar
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.guestNavToAuth

fun NavGraphBuilder.notification(
    cookbookViewModel: CookbookViewModel,
    navController: NavHostController
) {
    composable<Routes.Notifications> {
        cookbookViewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)
        cookbookViewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        cookbookViewModel.updateCanNavigateBack(true)

        val notificationViewModel = hiltViewModel<NotificationViewModel>()

        val uiState = notificationViewModel.notificationUiState.collectAsState().value
        Log.d("Notification", "notificationUiState: $uiState")

        Scaffold(
            topBar = {
                AppBarTheme {
                    NotificationScreenTopBar(
                        onBackButtonClick =
                        { navController.navigateUp() },
                        onClearAllClick = {
                            notificationViewModel.clearAllNotifications()
                        }
                    )
                }
            }
        ) { innerPadding ->
            when (uiState) {
                is ScreenUiState.Failure -> FailureScreen(uiState.message) { notificationViewModel.refresh() }
                ScreenUiState.Guest -> GuestLoginScreen { navController.guestNavToAuth() }
                ScreenUiState.Loading -> LoadingScreen()
                is ScreenUiState.Success ->
                    RefreshableScreen(
                        isRefreshing = notificationViewModel.isRefreshing.collectAsState().value,
                        onRefresh = { notificationViewModel.refresh() },
                    ) {
                        NotificationScreen(
                            notifications = uiState.data,
                            onNotificationClick = { notification ->
                                notificationViewModel.markRead(notification.id)
                                // TODO: Navigate to notification details
                                when (notification.type) {
                                    NotificationType.NEW_FOLLOWER -> navController.navigate(Routes.OtherProfile(notification.relatedId))
                                    NotificationType.NEW_POST_LIKE -> navController.navigate(Routes.App.PostDetails(notification.relatedId))
                                    NotificationType.NEW_POST_COMMENT -> navController.navigate(Routes.App.PostDetails(notification.relatedId))
                                    NotificationType.NEW_COMMENT_LIKE -> navController.navigate(Routes.App.PostDetails(notification.relatedId))
                                }
                            },
                            contentPadding = innerPadding,
                            loadMore = { notificationViewModel.loadMore() }
                        )
                    }
            }
        }
    }
}

