package com.example.androidcookbook.ui.features.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
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
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedCard
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.post.details.PostDetailsScreen


@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    searchUiState: SearchUiState,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        pageCount = { 2 }
    )
    BackHandler {
        when (searchUiState.currentScreen) {
            SearchScreenState.Food -> viewModel.ChangeScreenState(SearchScreenState.Waiting)
            SearchScreenState.Posts -> viewModel.ChangeScreenState(SearchScreenState.Food)
            SearchScreenState.Detail -> viewModel.ChangeScreenState(SearchScreenState.Food)
            SearchScreenState.Waiting -> onBackButtonClick()
        }
    }
    if (searchUiState.fail) {
        Text(
            text = searchUiState.result,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    } else {
        when (searchUiState.currentScreen) {
            SearchScreenState.Waiting -> {

            }
            SearchScreenState.Food -> {
                Column {
                    TabRow(
                        selectedTabIndex = pagerState.currentPage,
                        modifier = Modifier.weight(1f),
                        indicator = {
                            tabPositions ->
                            TabRowDefaults.Indicator(
                                modifier = Modifier.tabIndicatorOffset(
                                    currentTabPosition = tabPositions[pagerState.currentPage]
                                )
                            )
                        }
                    ) {
                        SearchTab.values().forEach {
                            Tab(
                                selected = pagerState.currentPage == it.ordinal,
                                onClick = {
                                    pagerState.requestScrollToPage(it.ordinal)
                                          },
                                text = { Text(text = it.name) }
                            )
                        }
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(12f),
                        verticalAlignment = Alignment.Top
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            when (it) {
                                0 -> {
                                    item {
                                        val posts = searchUiState.searchALlResults.posts
                                        val users = searchUiState.searchALlResults.users
                                        val length = posts.count() + users.count()
                                        var postsIndex = 0
                                        var usersIndex = 0
                                        while (postsIndex + usersIndex < length) {
                                            if (postsIndex < posts.count() && usersIndex < users.count()) {
                                                if (posts[postsIndex].id < users[usersIndex].id) {
                                                    PostCard(
                                                        post = posts[postsIndex],
                                                        onSeeDetailsClick = {
                                                            viewModel.ChangeCurrentPost(it)
                                                            viewModel.ChangeScreenState(SearchScreenState.Detail)
                                                        }
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
                                                    onSeeDetailsClick = {
                                                        viewModel.ChangeCurrentPost(it)
                                                        viewModel.ChangeScreenState(SearchScreenState.Detail)
                                                    }
                                                )
                                                postsIndex++
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))
                                        }
                                    }
                                }
                                1 -> {
                                    items(searchUiState.resultList) { item ->
                                        ResultCardTheme {
                                            ResultCard(
                                                onClick = {
                                                    viewModel.ChangeScreenState(SearchScreenState.Posts)
                                                },
                                                recipe = item
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            SearchScreenState.Posts -> {
                NewsfeedScreen(
                    posts = SamplePosts.posts,
                    onSeeDetailsClick = {
                        viewModel.ChangeCurrentPost(it)
                        viewModel.ChangeScreenState(SearchScreenState.Detail)
                    }
                )
            }
            SearchScreenState.Detail -> {
                PostDetailsScreen(searchUiState.currentPost, false, {}) //TODO
            }
        }

    }
}

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

@Preview
@Composable
fun CardPreview() {
    ResultCardTheme {
        ResultCard(
            {},
            Recipe(
                0,
                "Summer Dish",
                "Side",
                "Japanese",
                "",
                ""
            )
        )
    }
}

@Preview
@Composable
fun CardPreviewDark() {
    ResultCardTheme(darkTheme = true) {
        ResultCard(
            {},
            Recipe(
                0,
                "Summer Dish",
                "Side",
                "Japanese",
                "",
                ""
            )
        )
    }
}

@Preview
@Composable
fun UserCardPreview() {
    UserCard(
        onClick = {},
        user = User(
            bio = "hehehehehhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhehehehe"
        )
    )
}


