package com.example.androidcookbook.ui.features.newsfeed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun NewsfeedScreen(
    posts: List<Post>,
    onSeeDetailsClick: (Post) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            posts
        ) { post ->
            NewsfeedCard(post, onSeeDetailsClick = onSeeDetailsClick)
        }
    }
}

@Composable
fun NewsfeedCard(
    post: Post,
    onSeeDetailsClick: (Post) -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        PostHeader(post.author, post.createdAt)
        Text(
            text = post.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        AsyncImage(
//            painter = painterResource(R.drawable.place_holder_shrimp_post_image),
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.mainImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
        )
        Text(
            text = post.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "See Details >",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onSeeDetailsClick(post) }
        )
    }
}

@Composable
fun PostHeader(
    author: User,
    createdAt: String?,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(author.avatar)
                .crossfade(true)
                .build(),
//            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape),
//            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
            error = painterResource(R.drawable.default_avatar),
            placeholder = painterResource(R.drawable.default_avatar)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Spacing between icon and username
        Column {
            Text(
                text = author.name,
                fontSize = 19.sp,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = createdAt?: "",
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
@Preview
fun NewsfeedCardPreview() {
    AndroidCookbookTheme(darkTheme = false) {
        NewsfeedScreen(
            posts = listOf(
                Post(
                    id = 0,
                    author = User(),
                    title = "Shrimp salad cooking :)",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
                    cookTime = null,
                    mainImage = null,
                    createdAt = "01/28/2024",
                    totalView = 0,
                    totalLike = 0,
                    totalComment = 0,
                    ingredient = null,
                    steps = null,
                )
            ),
            {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
@Preview
fun NewsfeedCardPreviewDarkTheme() {
    AndroidCookbookTheme(darkTheme = true) {
        NewsfeedScreen(
            posts = listOf(
                Post(
                    id = 0,
                    author = User(),
                    title = "Shrimp salad cooking :)",
                    description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard.",
                    cookTime = null,
                    mainImage = null,
                    createdAt = "01/28/2024",
                    totalView = 0,
                    totalLike = 0,
                    totalComment = 0,
                    ingredient = null,
                    steps = null,
                )
            ),
            {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background))
    }
}
