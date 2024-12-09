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
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.androidcookbook.ui.components.aigen.AiUploadResultScreen
import com.example.androidcookbook.ui.components.aigen.CookingLoadingAnimation
import com.example.androidcookbook.ui.components.aigen.CookingTimeInput
import com.example.androidcookbook.ui.components.aigen.DashedLine
import com.example.androidcookbook.ui.components.aigen.IngredientsInput
import com.example.androidcookbook.ui.components.aigen.LoadingTextWithEllipsis
import com.example.androidcookbook.ui.components.aigen.MealTitleInput
import com.example.androidcookbook.ui.components.aigen.NoteInput
import com.example.androidcookbook.ui.components.aigen.PortionInput
import com.example.androidcookbook.ui.components.aigen.ServedAsInput
import com.example.androidcookbook.ui.components.aigen.setAnimation
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
        } else if (uiState.isDoneUploadingImage) {
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
        } else if (uiState.isDone) {
            item {

                FinalResultScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    result = aiGenViewModel.getImageInforJson()
                )
            }
        }

        if (uiState.isTakingInput || uiState.isDoneUploadingImage) {
            item {
                Button(
                    onClick = {
                        aiGenViewModel.updateIsDone()
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
fun FinalResultScreen(modifier: Modifier = Modifier, result: String) {
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
            Text(text = result, color = MaterialTheme.colorScheme.outline)
        }
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
                viewModel.updateIsDoneUploadingImage()
            }

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