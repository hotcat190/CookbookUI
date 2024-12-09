package com.example.androidcookbook.ui.features.userprofile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.data.mocks.SamplePosts
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.GUEST_ID
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedCard
import com.example.androidcookbook.ui.features.userprofile.components.FollowButton
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun UserProfileScreen(
    user: User,
    headerButton: @Composable () -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column(
                Modifier
                    .padding(bottom = 10.dp)
            ) {
                UserProfileHeader(
                    avatarPath = user.avatar,
                    bannerPath = user.banner,
                    headerButton = headerButton,
                )
                UserInfo(user,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick,
                )
            }
        }
        content()
    }

}

fun LazyListScope.userPostPortion(
    userPosts: List<Post>,
    currentUser: User,
    onEditPost: (Post) -> Unit,
    onDeletePost: (Post) -> Unit,
    onPostSeeDetailsClick: (Post) -> Unit,
    onUserClick: (User) -> Unit,
) {
    items(
        items = userPosts,
        key = { post -> post.id }
    ) { post ->
        NewsfeedCard(
            post = post,
            currentUser = currentUser,
            onEditPost = { onEditPost(post) },
            onDeletePost = { onDeletePost(post) },
            onSeeDetailsClick = onPostSeeDetailsClick,
            onUserClick = onUserClick,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
    }
    if (userPosts.isEmpty()) {
        item {
            Text(
                text = "No posts at the moment.",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun UserProfileHeader(
    avatarPath: String? = null,
    bannerPath: String? = null,
    headerButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
    ) {
        UserBanner(bannerPath)
        UserAvatar(avatarPath)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 10.dp, bottom = 10.dp)
        ) {
            headerButton()
        }
    }
}

private const val isFollowingPreview = false

@Composable
private fun UserBanner(
    bannerPath: String?,
) {
    AsyncImage(
//        painter = painterResource(id = R.drawable.image_5),
        model = ImageRequest.Builder(LocalContext.current)
            .data(bannerPath)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier =
        Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop,
        error = painterResource(id = R.drawable.image_5),
        placeholder = painterResource(id = R.drawable.image_5),
    )
}

@Composable
private fun UserAvatar(avatarPath: String?) {
    AsyncImage(
//            painter = painterResource(id = R.drawable.default_avatar),
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarPath)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier =
        Modifier
            .size(125.dp)
            .offset(x = 20.dp, y = 140.dp)
            .border(shape = CircleShape, width = 5.dp, color = Color.White)
            .padding(5.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop,
        placeholder = painterResource(R.drawable.default_avatar),
        error = painterResource(R.drawable.default_avatar),
    )
}

@Composable
fun UserInfo(
    user: User,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 30.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = user.name,
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight(600),
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (user.id == GUEST_ID) {
            return@Column
        }
        Row {
            Box(
                modifier = Modifier
                    .clickable {
                        onFollowersClick()
                    }
            ) {
                Text(
                    text = user.totalFollowers.toString(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700),
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = " followers",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clickable { onFollowingClick() }
            ) {
                Text(
                    text = user.totalFollowing.toString(),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(700),
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = " followings",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (user.bio.isEmpty()) {
            return@Column
        }
        Text(
            text = user.bio,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview
@Composable
fun ProfileDarkPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        UserProfileScreen(
            user = User(
                GUEST_ID,
                bio = "I like suffering",
                "Ly Duc",
                null,
                null,
                0,
                1
            ),
            headerButton = {
//                EditProfileButton(onEditProfileClick = {})
                FollowButton(onFollowButtonClick = {}, isFollowing = isFollowingPreview)
            },
            onFollowersClick = {},
            onFollowingClick = {},
            content = {
                userPostPortion(
                    userPosts = SamplePosts.posts,
                    currentUser = User(),
                    onEditPost = {},
                    onDeletePost = {},
                    onPostSeeDetailsClick = {},
                    onUserClick = {},
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    AndroidCookbookTheme(darkTheme = false) {
        UserProfileScreen(
            user = User(
                1,
                bio = "I like suffering",
                "Ly Duc",
                null,
                null,
                0,
                1
            ),
            headerButton = {
//                EditProfileButton(onEditProfileClick = {})
                FollowButton(onFollowButtonClick = {}, isFollowing = isFollowingPreview)
            },
            onFollowersClick = {},
            onFollowingClick = {},
            content = {
                userPostPortion(
                    userPosts = SamplePosts.posts,
                    currentUser = User(),
                    onEditPost = {},
                    onDeletePost = {},
                    onPostSeeDetailsClick = {},
                    onUserClick = {},
                )
            }
        )
    }
}

@Preview
@Composable
fun AvtPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        UserProfileHeader(
            avatarPath = null,
            bannerPath = null,
            headerButton = {
//                EditProfileButton(onEditProfileClick = {})
                FollowButton(onFollowButtonClick = {}, isFollowing = isFollowingPreview)
            },
        )
    }
}

@Preview()
@Composable
fun UserInfoPreview() {
    AndroidCookbookTheme(darkTheme = true) {
        UserInfo(
            User(
                1,
                bio = "I like suffering",
                "Ly Duc",
                null,
                null,
                0,
                1,
                ),
            onFollowersClick = {},
            onFollowingClick = {},
        )
    }
}