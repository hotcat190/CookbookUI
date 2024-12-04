package com.example.androidcookbook.ui.features.aigen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.androidcookbook.R
import com.example.androidcookbook.domain.model.aigen.AiRecipe
import com.example.androidcookbook.ui.components.aigen.CookingTimeInput
import com.example.androidcookbook.ui.components.aigen.IngredientsInput
import com.example.androidcookbook.ui.components.aigen.MealTitleInput
import com.example.androidcookbook.ui.components.aigen.PortionInput
import com.example.androidcookbook.ui.components.aigen.ServedAsInput
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@Composable
fun AIGenScreen(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val aiGenViewModel: AiGenViewModel = hiltViewModel()
    val uiState by aiGenViewModel.aiGenUiState.collectAsState()
    val selectedImageUri by aiGenViewModel.selectedImageUri.collectAsState()
    var testJson = ""

    val uploadResult by aiGenViewModel.uploadResponse.collectAsState()

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        item {
            Text(
                text = "AI Chef",
                fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 36.sp
            )
        }

        if (uiState.isTakingInput) {
            item {
                TakingInputScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    viewModel = aiGenViewModel,
                    uiState = uiState
                )
            }
        } else if (uiState.isDone) {
            item {
                AiResultScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    result = uploadResult,
                    imageUri = selectedImageUri
                )
            }
        }

        item {
            Button(onClick = {
                testJson = aiGenViewModel.getUiStateJson()
                scope.launch {
                    val file = getFileFromUri(context, aiGenViewModel.selectedImageUri.value)
                    file?.let {
                        val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
                        val body = MultipartBody.Part.createFormData("image", it.name, requestFile)
                        aiGenViewModel.uploadImage(body)
                    }
                }
                aiGenViewModel.updateIsDone()
            }) { }
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
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri: Uri? = result.data?.data
            viewModel.updateSelectedUri(uri)
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .background(color = Color(0xFFF9F4F1))
                .background(color = MaterialTheme.colorScheme.surfaceContainer)
//                .border(width = 1.dp, color = Color(0xFF7F5346))
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

                Spacer(Modifier.weight(1f))

                IconButton(onClick = {
                    val intent = Intent(Intent.ACTION_PICK).apply {
                        type = "image/*"
                    }
                    launcher.launch(intent)
                },
                    modifier = Modifier.weight(1f).padding(top = 12.dp)) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.camera_icon),
                        contentDescription = "Camera Icon"
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            //TODO Dashed line
            DashedLine(
                color = Color.Black,
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
                color = Color.Black,
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
fun AiResultScreen(modifier: Modifier = Modifier, result: AiRecipe?, imageUri: Uri?) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFF9F4F1))
                .border(width = 1.dp, color = Color(0xFF7F5346))
                .padding(top = 24.dp, bottom = 12.dp, start = 24.dp, end = 24.dp),
        ) {
            Text(
                text = "Recipe Details",
                fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (result != null) {
                Text(
                    text = "Ingredients:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.size(8.dp))
                result.ingredients.forEach { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "- ${ingredient.name}",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${ingredient.quantity} ",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Text(
                    text = "Recipes:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                result.recipes.forEach {
                    recipe ->


                    Spacer(modifier = Modifier.size(8.dp))

                    Text(
                        text = "- ${recipe}",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.weight(1f))

                }
             } else {

                Text(
                    text = "No ingredients available",
                    color = Color.Gray,
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
                        .size(200.dp)
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                )
            } else {
                Text(
                    text = "No image selected",
                    style = TextStyle(color = Color.Gray, fontSize = 16.sp)
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
            fontWeight = FontWeight.Bold
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
        val inputStream = uri?.let { context.contentResolver.openInputStream(it) } ?: return null
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