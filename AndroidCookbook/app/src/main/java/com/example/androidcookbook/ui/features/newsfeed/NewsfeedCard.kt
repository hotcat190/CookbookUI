package com.example.androidcookbook.ui.features.newsfeed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidcookbook.domain.model.post.Post
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.common.utils.apiDateFormatter
import com.example.androidcookbook.ui.components.post.PostHeader
import com.example.androidcookbook.ui.components.post.PostTitle
import java.time.LocalDate

@Composable
fun NewsfeedCard(
    post: Post,
    currentUser: User,
    onEditPost: () -> Unit,
    onDeletePost: () -> Unit,
    onUserClick: (User) -> Unit,
    onSeeDetailsClick: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
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
            onUserClick = onUserClick,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            PostTitle(post.title)
            if (post.mainImage != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(post.mainImage)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .padding(top = 16.dp, bottom = 8.dp)
                )
            }
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "See Details >",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(MaterialTheme.shapes.small)
                    .clickable { onSeeDetailsClick(post) }
            )
        }
    }
}

