package com.example.androidcookbook.ui.features.search

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedCard

@Composable
fun ResultCard(
    onClick: () -> Unit,
    recipe: Recipe
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Row(
            modifier = Modifier.height(120.dp)
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(recipe.strMealThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = recipe.strMeal,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = recipe.strCategory,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = recipe.strArea,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun UserCard(
    onClick: () -> Unit,
    user: User
) {
    ResultCardTheme {
        Card(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainer)
        ) {
            Row(
                modifier = Modifier.height(150.dp)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(10.dp)
                        .clip(CircleShape)
                        .weight(1f),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(user.avatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img),
                )
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(2f)
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = user.name,
                        modifier = Modifier
                            .weight(2f)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight()
                            .align(Alignment.Start),
                    ) {
                        Text(
                            text = user.totalFollowers.toString(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " followers",
                            fontSize = 15.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.Start),
                    ) {
                        Text(
                            text = user.totalFollowing.toString(),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = " followings",
                            fontSize = 15.sp
                        )
                    }
                    if (user.bio == null) {
                        return@Column
                    }
                    Text(
                        text = user.bio,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        fontSize = 15.sp,
                        softWrap = true
                    )
                }
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    onSeeDetailsClick: (Post) -> Unit
) {
    ResultCardTheme {
        NewsfeedCard(
            post = post,
            onSeeDetailsClick = onSeeDetailsClick,
            currentUser = User(),
            onDeletePost = {},
            onEditPost = {},
            onUserClick = {},
            modifier = Modifier
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    color = MaterialTheme.colorScheme.outline,
                    width = 2.dp,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@Composable
fun SearchAllResultsScreen(
    posts: List<Post>,
    users: List<User>,
    onSeeDetailsClick: (Post) -> Unit
) {
    val length = posts.count() + users.count()
    var postsIndex = 0
    var usersIndex = 0
    while (postsIndex + usersIndex < length) {
        if (postsIndex < posts.count() && usersIndex < users.count()) {
            if (posts[postsIndex].id < users[usersIndex].id) {
                PostCard(
                    post = posts[postsIndex],
                    onSeeDetailsClick = onSeeDetailsClick
                )
                postsIndex++
            } else {
                UserCard(
                    onClick = {},
                    user = users[usersIndex]
                )
                usersIndex++
            }
        } else if (postsIndex >= posts.count() && usersIndex < users.count()) {
            UserCard(
                onClick = {},
                user = users[usersIndex]
            )
            usersIndex++
        } else if (usersIndex >= users.count() && postsIndex < posts.count()) {
            PostCard(
                post = posts[postsIndex],
                onSeeDetailsClick = onSeeDetailsClick
            )
            postsIndex++
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}