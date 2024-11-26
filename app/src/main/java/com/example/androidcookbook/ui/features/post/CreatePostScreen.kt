package com.example.androidcookbook.ui.features.post

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.androidcookbook.domain.model.user.User
import com.example.androidcookbook.ui.features.newsfeed.PostHeader
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme

@Composable
fun CreatePostScreen(
    postTitle: String,
    updatePostTitle: (String) -> Unit,
    postBody: String,
    updatePostBody: (String) -> Unit,
    postImageUri: Uri?,
    updatePostImageUri: (Uri?) -> Unit,
    onPostButtonClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler { onBackButtonClick() }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = updatePostImageUri
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PostHeader(
            author = User(),
            createdAt = "01/28/2024",
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Post Title
        TextField(
            value = postTitle,
            onValueChange = updatePostTitle,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "What's new !",
                )
            },
            textStyle = MaterialTheme.typography.titleMedium,
            singleLine = true,
        )

        if (postImageUri != null) {
            AsyncImage(
                model = postImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
            )
        } else {
            Button(
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            ) {
                Text("Add image")
            }
        }

//        Image(
//            painter = painterResource(R.drawable.place_holder_shrimp_post_image),
//            contentDescription = null,
//            contentScale = ContentScale.FillWidth,
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(RoundedCornerShape(16.dp))
//                .padding(vertical = 16.dp)
//        )

        // Post Body
        TextField(
            value = postBody,
            onValueChange = updatePostBody,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Post body"
                )
            },
            minLines = 5,
        )

        Button(
            onClick = onPostButtonClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 8.dp)
        ) {
            Text(text = "Post")
        }
    }
}

@Composable
@Preview
fun CreatePostScreenPreview() {
    AndroidCookbookTheme(darkTheme = false) {
        CreatePostScreen(
            "", {},
            "", {},
            null, {},
            {},
            {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Composable
@Preview
fun CreatePostScreenPreviewDarkTheme() {
    AndroidCookbookTheme(darkTheme = true) {
        CreatePostScreen(
            "", {},
            "", {},
            null, {},
            {},
            {},
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}