package com.example.androidcookbook.ui.features.aigen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.aigen.AiRecipe
import com.example.androidcookbook.domain.usecase.createImageRequestBody
import com.example.androidcookbook.ui.components.aigen.CookingLoadingAnimation
import com.example.androidcookbook.ui.components.aigen.CookingTimeInput
import com.example.androidcookbook.ui.components.aigen.IngredientsInput
import com.example.androidcookbook.ui.components.aigen.LoadingTextWithEllipsis
import com.example.androidcookbook.ui.components.aigen.MealTitleInput
import com.example.androidcookbook.ui.components.aigen.PortionInput
import com.example.androidcookbook.ui.components.aigen.ServedAsInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AIGenScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val aiGenViewModel: AiGenViewModel = hiltViewModel()
    val uiState by aiGenViewModel.aiGenUiState.collectAsState()
    val selectedImageUri by aiGenViewModel.selectedImageUri.collectAsState()




    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {


        if (uiState.isTakingInput || uiState.isDone) {
            item {
                Text(
                    text = "AI Chef",
                    fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 36.sp
                )
            }
        }

        if (uiState.isTakingInput) {

            item {
                TakingInputScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    viewModel = aiGenViewModel,
                    uiState = uiState,
                    context = context,
                    scope = scope,
                )
            }


        } else if (uiState.isProcessing) {
            item {
                setAnimation()
            }
        } else if (uiState.isDone) {
            item {
                AiUploadResultScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    result = aiGenViewModel.uploadResponse.value,

                    imageUri = selectedImageUri,
                    uiState = uiState,
                    viewModel = aiGenViewModel
                )
            }
        }

        if (uiState.isTakingInput) {
            item {
                Button(
                    onClick = {


                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = Color.Gray,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    Text(
                        "Let's cook", fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

@Composable
fun setAnimation() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.size(200.dp))


        CookingLoadingAnimation()
        LoadingTextWithEllipsis()


    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TakingInputScreen(
    modifier: Modifier = Modifier,
    uiState: AIGenUiState,
    viewModel: AiGenViewModel,
    context: Context,
    scope: CoroutineScope
) {


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {


            scope.launch {
                val uri: Uri? = result.data?.data
                viewModel.updateSelectedUri(uri)
                viewModel.updateIsProcessing()

                val body = uri?.let { createImageRequestBody(context, it) }

                val result = body?.let { viewModel.uploadImage(it) }

                if (result != null) {
                    Log.d("Upload", "Upload successful")
                    viewModel.uploadResponse.value?.ingredients?.forEach { ingredient ->
                        viewModel.addIngredient(ingredient)
                    }
                } else {
                    Log.d("Upload", "Upload failed.")
                }
            }
            viewModel.updateIsDone()

        }
    }


    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
                .padding(top = 24.dp, bottom = 12.dp, start = 24.dp, end = 24.dp),
        ) {

            //TODO Meal Title
            MealTitleInput(
                mealTitle = uiState.mealTitle,
                onMealTitleChange = { viewModel.updateMealTitle(it) })


            //TODO Cooking time
            Row {
                CookingTimeInput(
                    modifier = Modifier.weight(2f),
                    cookingTime = uiState.cookingTime,
                    onCookingTimeChange = { viewModel.updateCookingTime(it) },
                    selectedOption = uiState.timeMeasurement,
                    onOptionsClick = { viewModel.updateTimeMeasurement(it) })

                Spacer(Modifier.weight(0.2f))
                //TODO Portion

                PortionInput(modifier = Modifier
                    .padding(bottom = 4.dp)
                    .weight(0.6f),
                    portion = uiState.portion,
                    onPortionChange = { viewModel.updatePortion(it) })

            }

            //TODO Served As
            Row(verticalAlignment = Alignment.CenterVertically) {
                ServedAsInput(
                    modifier = Modifier.weight(2f),
                    selectedOption = uiState.servedAs,
                    onServedAsClick = { viewModel.updateServedAs(it) })

                Spacer(Modifier.weight(1.5f))

                IconButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                        }
                        launcher.launch(intent)

                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp)
                ) {

                    Icon(
                        painter = painterResource(R.drawable.gallery_icon),
                        contentDescription = "Camera"
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            //TODO Dashed line
            DashedLine(
                color = MaterialTheme.colorScheme.outline,
                dashWidth = 6f,
                dashGap = 6f,
                strokeWidth = 2f
            )


            Spacer(modifier = Modifier.size(8.dp))

            //TODO Ingredient adding
            IngredientsInput(
                ingredients = uiState.ingredients,
                onIngredientNameChange = { index, it -> viewModel.updateIngredientName(index, it) },
                onIngredientQuantityChange = { index, it ->
                    viewModel.updateIngredientQuantity(
                        index,
                        it
                    )
                },
                onDeleteIngredient = { viewModel.deleteIngredient(it) },
                addIngredient = { viewModel.addEmptyIngredient() })


            DashedLine(
                color = MaterialTheme.colorScheme.outline,
                dashWidth = 6f,
                dashGap = 6f,
                strokeWidth = 2f
            )

            Spacer(modifier = Modifier.size(8.dp))

            NoteInput(note = uiState.note, onNoteChange = { viewModel.updateNote(it) })
        }
    }
}

@Composable
fun AiUploadResultScreen(
    modifier: Modifier = Modifier,
    result: AiRecipe?,
    imageUri: Uri?,
    uiState: AIGenUiState,
    viewModel: AiGenViewModel,
) {
    Box(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
                .border(width = 1.dp, color = Color(0xFF7F5346))
                .padding(top = 24.dp, bottom = 12.dp, start = 24.dp, end = 24.dp),
        ) {

            Text(
                text = "Recipe Details",
                fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (result != null) {
                Text(
                    text = "Ingredients:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.size(8.dp))



                IngredientsInput(
                    ingredients = uiState.ingredients,
                    onIngredientNameChange = { index, it ->
                        viewModel.updateIngredientName(
                            index,
                            it
                        )
                    },
                    onIngredientQuantityChange = { index, it ->
                        viewModel.updateIngredientQuantity(
                            index,
                            it
                        )
                    },
                    onDeleteIngredient = { viewModel.deleteIngredient(it) },
                    addIngredient = { viewModel.addEmptyIngredient() })

                Text(
                    text = "Recipes:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.outline
                )

                result.recipes.forEach { recipe ->


                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "- ${recipe}",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.weight(1f))

                }
            } else {

                Text(
                    text = "No ingredients available",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.size(16.dp))

            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1.5f)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clip(RoundedCornerShape(8.dp))
                )

            }
        }
    }
}


@Composable
fun NoteInput(modifier: Modifier = Modifier, note: String, onNoteChange: (String) -> Unit) {


    Column(modifier = Modifier.padding(bottom = 4.dp)) {

        Text(
            text = "*Note",
            fontFamily = FontFamily(Font(R.font.nunito_regular)),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.outline
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 128.dp),
            value = note,
            onValueChange = onNoteChange,
            placeholder = {
                Text(
                    "Tell us what you want about this dish...",

                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFFFFF).copy(alpha = 0.75f),
                    fontSize = 14.sp

                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                textColor = Color.White,
                backgroundColor = Color(0xFF4A4A4A)
            ),
            shape = RoundedCornerShape(4.dp),

            )
    }

}


@Composable
fun DashedLine(
    color: Color = Color.Gray,
    dashWidth: Float = 10f,
    dashGap: Float = 10f,
    strokeWidth: Float = 2f
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp) // Adjust height for thickness
    ) {
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(0f, size.height / 2),
            end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashWidth, dashGap), 0f)
        )
    }
}


fun getFileFromUri(context: android.content.Context, uri: Uri?): File? {
    return try {
        val inputStream =
            uri?.let { context.contentResolver.openInputStream(it) } ?: return null
        val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
@Preview
fun AIGenScreenPreview() {
    AiScreenTheme {
        AIGenScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
@Preview
fun AIGenScreenDarkPreview() {
    AiScreenTheme(darkTheme = true) {
        AIGenScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}