package com.example.androidcookbook.ui.features.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.androidcookbook.ui.features.newsfeed.NewsfeedScreen
import com.example.androidcookbook.ui.features.post.details.PostDetailsScreen


@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    searchUiState: SearchUiState,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    BackHandler {
        when (searchUiState.currentScreen) {
            SearchScreenState.Food -> onBackButtonClick()
            SearchScreenState.Posts -> viewModel.ChangeScreenState(SearchScreenState.Food)
            SearchScreenState.Detail -> viewModel.ChangeScreenState(SearchScreenState.Posts)
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
            SearchScreenState.Food -> {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
            SearchScreenState.Posts -> {
//                NewsfeedScreen(
//                    posts = SamplePosts.posts,
//                    onSeeDetailsClick = {
//                        viewModel.ChangeScreenState(SearchScreenState.Detail)
//                    }
//                )
            }
            SearchScreenState.Detail -> {
//                PostDetailsScreen(Post(), false, {}, {}) //TODO
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


