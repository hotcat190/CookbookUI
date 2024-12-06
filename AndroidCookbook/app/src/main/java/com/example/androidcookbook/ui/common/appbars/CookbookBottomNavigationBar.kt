package com.example.androidcookbook.ui.common.appbars

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    Column {
        HorizontalDivider(
            thickness = 1.dp,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface.copy(0.25F)
        )

        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .heightIn(max = 104.dp)
        ) {
            NewsfeedNavigationBarItem(currentDestination, onNewsfeedClick)

            AiChatNavigationBarItem(currentDestination, onAiChatClick)

            NavigationBarItem(
                selected = currentDestination?.hasRoute(Routes.CreatePost) == true,
                onClick = onCreatePostClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create post"
                    )
                },
                alwaysShowLabel = false,
            )

            CategoryNavigationBarItem(currentDestination, onCategoryClick)

            UserProfileNavigationBarItem(currentDestination, onUserProfileClick)
        }
    }
}

@Composable
private fun RowScope.CookbookNavigationBarItem(
    currentDestination: NavDestination?,
    route: Any,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    alwaysShowLabel: Boolean,
) {
    NavigationBarItem(
        selected = currentDestination?.hasRoute(route) == true,
        onClick = onClick,
        icon = icon,
//        label = label,
        alwaysShowLabel = alwaysShowLabel,
    )
}

@Composable
private fun RowScope.UserProfileNavigationBarItem(
    currentDestination: NavDestination?,
    onUserProfileClick: () -> Unit,
) {
    CookbookNavigationBarItem(
        currentDestination = currentDestination,
        route = Routes.App.UserProfile(0),
        onClick = onUserProfileClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_user_profile),
                contentDescription = "User Profile"
            )
        },
        label = {
            Text(
                text = "Me",
                maxLines = 1,
            )
        },
        alwaysShowLabel = false,
    )
}

@Composable
private fun RowScope.NewsfeedNavigationBarItem(
    currentDestination: NavDestination?,
    onNewsfeedClick: () -> Unit,
) {
    CookbookNavigationBarItem(
        currentDestination,
        route = Routes.App.Newsfeed,
        onClick = onNewsfeedClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_newsfeed),
                contentDescription = "Newsfeed"
            )
        },
        label = {
            Text(
                text = "Me",
                maxLines = 1,
            )
        },
        alwaysShowLabel = false,
    )
}

@Composable
private fun RowScope.AiChatNavigationBarItem(
    currentDestination: NavDestination?,
    onChatClick: () -> Unit,
) {
    CookbookNavigationBarItem(
        currentDestination = currentDestination,
        route = Routes.App.AIChat,
        onClick = onChatClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_chat),
                contentDescription = "Chat"
            )
        },
        label = {
            Text(
                text = Routes.App.AIChat::class.java.simpleName,
                maxLines = 1
            )
        },
        alwaysShowLabel = false,
    )
}

@Composable
private fun RowScope.CategoryNavigationBarItem(
    currentDestination: NavDestination?,
    onCategoryClick: () -> Unit,
) {
    CookbookNavigationBarItem(
        currentDestination = currentDestination,
        route = Routes.App.Category,
        onClick = onCategoryClick,
        icon = {
            Icon(
                painter = painterResource(R.drawable.icon_category),
                contentDescription = "Home"
            )
        },
        label = {
            Text(
                text = Routes.App.Category::class.java.simpleName,
                maxLines = 1
            )
        },
        alwaysShowLabel = false,
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