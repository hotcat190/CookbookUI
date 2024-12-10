package com.example.androidcookbook.ui.features.follow

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.data.mocks.SampleUser
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme
import kotlinx.serialization.Serializable

@Serializable
enum class FollowListScreenType {
    Followers,
    Following
}

@Composable
fun BaseFollowListScreen(
    user: User,
    type: FollowListScreenType,
    list: List<User>,
    onListItemClick: (User) -> Unit,
    isFollowing: (User) -> Boolean,
    onFollowButtonClick: (User) -> Unit,
    onBackButtonClick: () -> Unit,
    onFollowingNavigate: () -> Unit,
    onFollowersNavigate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column {
        FollowScreenTopBar(
            onBackButtonClick,
            user,
            type,
            onFollowingNavigate = onFollowingNavigate,
            onFollowersNavigate = onFollowersNavigate,
            modifier = Modifier,
        )
        LazyColumn(
            modifier = modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .weight(1F),
        ) {
            items(
                items = list,
                key = { user -> user.id }
            ) { user ->
                FollowingListItem(
                    user = user,
                    onClick = { onListItemClick(user) },
                    isFollowing = isFollowing(user),
                    onFollowButtonClick = { onFollowButtonClick(user) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }


}

@Composable
private fun FollowScreenTopBar(
    onBackButtonClick: () -> Unit,
    user: User,
    type: FollowListScreenType,
    onFollowingNavigate: () -> Unit,
    onFollowersNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackButtonClick,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "${user.name}'s $type"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FollowScreenNavigationItem(
                text = "Followers",
                selected = type == FollowListScreenType.Followers,
                onClick = onFollowersNavigate,
                modifier = Modifier
                    .weight(1F)
            )
            FollowScreenNavigationItem(
                text = "Following",
                selected = type == FollowListScreenType.Following,
                onClick = onFollowingNavigate,
                modifier = Modifier
                    .weight(1F)
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun FollowScreenNavigationItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable {
                onClick()
            }
            .fillMaxHeight()
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            color = (if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified)
        )
        if (selected) {
            val color = MaterialTheme.colorScheme.primary
            Canvas(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(4.dp)
                    .requiredWidth(75.dp)
            ) {
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(50F, 50f)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BaseFollowListScreenPreview() {
    AndroidCookbookTheme {
        var type by remember { mutableStateOf(FollowListScreenType.Followers) }
        BaseFollowListScreen(
            user = SampleUser.users[0],
            type = FollowListScreenType.Followers,
            list = SampleUser.users,
            onListItemClick = {},
            isFollowing = { false },
            onFollowButtonClick = {},
            onBackButtonClick = {},
            onFollowingNavigate = {type = FollowListScreenType.Following},
            onFollowersNavigate = {type = FollowListScreenType.Followers},
            modifier = Modifier
        )
    }
}