package com.example.androidcookbook.ui.features.newsfeed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.data.mocks.SamplePosts
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.common.utils.apiDateFormatter
import com.example.androidcookbook.ui.components.post.PostHeader
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme
import java.time.LocalDate

@Composable
fun NewsfeedScreen(
    posts: List<Post>,
    currentUser: User,
    onEditPost: (Post) -> Unit,
    onDeletePost: (Post) -> Unit,
    onSeeDetailsClick: (Post) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            posts
        ) { post ->
            NewsfeedCard(
                post = post,
                currentUser = currentUser,
                onEditPost = { onEditPost (post) },
                onDeletePost= { onDeletePost(post) },
                onSeeDetailsClick = onSeeDetailsClick)
        }
    }
}

@Composable
fun NewsfeedCard(
    post: Post,
    currentUser: User,
    onEditPost: () -> Unit,
    onDeletePost: () -> Unit,
    onSeeDetailsClick: (Post) -> Unit,
    modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        PostHeader(
            post.author,
            LocalDate.parse(post.createdAt, apiDateFormatter).toString(),
            showOptionsButton = currentUser.id == post.author.id,
            onEditPost = onEditPost,
            onDeletePost = onDeletePost,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
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
}

@Composable
@Preview
fun NewsfeedCardPreview() {
    AndroidCookbookTheme(darkTheme = false) {
        NewsfeedScreen(
            posts = SamplePosts.posts,
            currentUser = User(),
            {}, {}, {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
@Preview
fun NewsfeedCardPreviewDarkTheme() {
    AndroidCookbookTheme(darkTheme = true) {
        NewsfeedScreen(
            posts = SamplePosts.posts,
            currentUser = User(),
            {}, {}, {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
