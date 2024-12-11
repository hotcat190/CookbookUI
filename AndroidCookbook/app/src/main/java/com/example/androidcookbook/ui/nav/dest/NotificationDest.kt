package com.example.androidcookbook.ui.nav.dest

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.androidcookbook.data.mocks.SampleNotifications
import com.example.androidcookbook.ui.CookbookUiState
import com.example.androidcookbook.ui.CookbookViewModel
import com.example.androidcookbook.ui.features.notification.NotificationScreen
import com.example.androidcookbook.ui.nav.Routes

fun NavGraphBuilder.notification(
    cookbookViewModel: CookbookViewModel,
    navController: NavHostController
) {
    composable<Routes.Notifications> {
        cookbookViewModel.updateTopBarState(CookbookUiState.TopBarState.NoTopBar)
        cookbookViewModel.updateBottomBarState(CookbookUiState.BottomBarState.NoBottomBar)
        cookbookViewModel.updateCanNavigateBack(true)

        NotificationScreen(
            notifications = SampleNotifications.notifications,
            onNotificationClick = { },
            onClearAllClick = { },
            onBackButtonClick = { navController.navigateUp() }
        )
    }
}