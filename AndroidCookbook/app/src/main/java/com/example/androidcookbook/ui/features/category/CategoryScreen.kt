package com.example.androidcookbook.ui.features.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.category.Category
import com.example.androidcookbook.domain.model.recipe.Recipe
import com.example.androidcookbook.ui.common.containers.RefreshableScreen
import com.example.androidcookbook.ui.components.category.CategoryScreenLoading
import com.example.androidcookbook.ui.theme.Typography

const val CATEGORY_SCREEN_TAG = "CategoryScreen"

@Composable
fun CategoryScreen(
    categoryViewModel: CategoryViewModel,
    modifier: Modifier = Modifier,
) {
    RefreshableScreen(
        onRefresh = { categoryViewModel.refresh() }
    ) {
        when (val categoryUiState = categoryViewModel.categoryUiState.collectAsState().value) {
            is CategoryUiState.Loading -> {
                CategoryScreenLoading()
            }

            is CategoryUiState.Success -> {
                CategoryListScreen(
                    categories = categoryUiState.categories,
                    randomMeals = categoryUiState.randomMeals,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 8.dp, top = 16.dp, end = 8.dp)
                )
            }

            is CategoryUiState.Error -> Text("Error")
        }
    }
}



@Composable
fun CategoryCard(category: Category, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
//        border = BorderStroke(2.dp, Color(0xFFE8D6D2))
    ) {
        Box {
            // Image Background
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Adjust height as needed
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(category.strCategoryThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )

            // Text Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = category.strCategory,
                    style = Typography.titleSmall.copy(color = Color.White),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}


@Composable
fun RandomMealCard(randomMeal: Recipe, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
//        border = BorderStroke(2.dp, Color(0xFFE8D6D2))
    ) {
        Box {
            // Image Background
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp), // Adjust height as needed
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(randomMeal.strMealThumb)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.loading_img)
            )

            // Text Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = randomMeal.strMeal,
                    style = Typography.titleSmall.copy(color = Color.White),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryListScreen(
    categories: List<Category>,
    randomMeals: List<Recipe>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            // Full-width Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp) // Optional padding
            ) {
                Text(
                    text = "Discover new dishes",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterStart) // Align text if needed
                )
            }
        }

        items(items = randomMeals) { randomMeal ->
            RandomMealCard(randomMeal = randomMeal, Modifier.fillMaxSize())

        }
        item {
            Text(
                text = "Category",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
        item { }
        items(
            items = categories,
        ) { category ->
            CategoryCard(category = category, modifier = Modifier.fillMaxSize())
        }
    }
}

