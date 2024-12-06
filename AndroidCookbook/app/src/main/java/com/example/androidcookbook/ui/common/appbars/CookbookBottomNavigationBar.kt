package com.example.androidcookbook.ui.common.appbars

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.nav.Routes
import com.example.androidcookbook.ui.nav.utils.hasRoute
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun CookbookBottomNavigationBar(
    onCategoryClick: () -> Unit,
    onAiChatClick: () -> Unit,
    onNewsfeedClick: () -> Unit,
    onUserProfileClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    currentDestination: NavDestination? = null,
) {
    NavigationBar (
        containerColor = Color.Transparent,
        modifier = Modifier.heightIn(max = 96.dp)
    ) {
        NewsfeedNavigationBarItem(currentDestination, onNewsfeedClick)

        AiChatNavigationBarItem(currentDestination, onAiChatClick)

        NavigationBarItem(
            selected = currentDestination?.hasRoute(Routes.CreatePost) == true,
            onClick = onCreatePostClick,
            icon = {
                Icon(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Create post",
                    modifier = Modifier.size(18.dp)
                )
            },
            alwaysShowLabel = false,
        )

        CategoryNavigationBarItem(currentDestination, onCategoryClick)

        UserProfileNavigationBarItem(currentDestination, onUserProfileClick)
    }
}

@Composable
private fun RowScope.UserProfileNavigationBarItem(
    currentDestination: NavDestination?,
    onUserProfileClick: () -> Unit,
) {
    NavigationBarItem(
        selected = currentDestination?.hasRoute(Routes.App.UserProfile(0)) == true,
        onClick = onUserProfileClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_user_profile),
                contentDescription = "User Profile"
            )
        },
//        label = {
//            Text(
//                text = "Me",
//                maxLines = 1,
//            )
//        },
        alwaysShowLabel = true,
    )
}

@Composable
private fun RowScope.NewsfeedNavigationBarItem(
    currentDestination: NavDestination?,
    onNewsfeedClick: () -> Unit,
) {
    NavigationBarItem(
        selected = currentDestination?.hasRoute(Routes.App.Newsfeed) == true,
        onClick = onNewsfeedClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_newsfeed),
                contentDescription = "Newsfeed",
                modifier = Modifier
            )
        },
//        label = {
//            Text(
//                text = Routes.App.Newsfeed::class.java.simpleName,
//                maxLines = 1
//            )
//        },
        alwaysShowLabel = true,
    )
}

@Composable
private fun RowScope.AiChatNavigationBarItem(
    currentDestination: NavDestination?,
    onChatClick: () -> Unit,
) {
    NavigationBarItem(
        selected = currentDestination?.hasRoute(Routes.App.AIChat) == true,
        onClick = onChatClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_chat),
                contentDescription = "Chat"
            )
        },
//        label = {
//            Text(
//                text = Routes.App.AIChat::class.java.simpleName,
//                maxLines = 1
//            )
//        },
        alwaysShowLabel = true,
    )
}

@Composable
private fun RowScope.CategoryNavigationBarItem(
    currentDestination: NavDestination?,
    onHomeClick: () -> Unit,
) {
    NavigationBarItem(
        selected = currentDestination?.hasRoute(Routes.App.Category) == true,
        onClick = onHomeClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_category),
                contentDescription = "Home"
            )
        },
//        label = {
//            Text(
//                text = Routes.App.Category::class.java.simpleName,
//                maxLines = 1
//            )
//        },
        alwaysShowLabel = true,
    )
}

@Preview
@Composable
fun NavBarPreview() {
    AndroidCookbookTheme {
        CookbookBottomNavigationBar({}, {}, {}, {}, {})
    }
}

@Preview
@Composable
fun NavBarDarkPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        CookbookBottomNavigationBar({}, {}, {}, {}, {})
    }
}