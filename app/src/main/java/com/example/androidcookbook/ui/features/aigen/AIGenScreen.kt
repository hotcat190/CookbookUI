package com.example.androidcookbook.ui.features.aigen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidcookbook.R
import com.example.androidcookbook.ui.components.aigen.AiGenInputLabel
import com.example.androidcookbook.ui.components.aigen.CookingTimeInput
import com.example.androidcookbook.ui.components.aigen.MealTitleInput
import com.example.androidcookbook.ui.components.aigen.PortionInput
import com.example.androidcookbook.ui.components.aigen.ServedAsInput


@Composable
fun AIGenScreen(modifier: Modifier = Modifier) {

    val aiGenViewModel: AiGenViewModel = viewModel()

    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        item {
            Text(
                text = "AI Chef",
                fontFamily = FontFamily(Font(R.font.playfairdisplay_regular)),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 36.sp
            )
        }
        item {
            TakingInputScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
            )
        }

        item {
            Button(onClick = {}) { }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TakingInputScreen(
    modifier: Modifier = Modifier,
    mealTitle: String = "",
    onMealTitleChange: (String) -> Unit = {},
) {
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

            //TODO Meal Title
            MealTitleInput(mealTitle = mealTitle, onMealTitleChange = onMealTitleChange)


            //TODO Cooking time
            Row {
                CookingTimeInput(
                    modifier = Modifier.weight(2f),
                    cookingTime = "",
                    onCookingTimeChange = {})

                Spacer(Modifier.weight(0.8f))

                //TODO Portion

                PortionInput(modifier = Modifier
                    .padding(bottom = 4.dp)
                    .weight(1f), portion = "", onPortionChange = {})

            }

            //TODO Served As
            ServedAsInput()


            Spacer(modifier = Modifier.size(8.dp))

            //TODO Dashed line
            DashedLine(
                color = Color.Black,
                dashWidth = 6f,
                dashGap = 6f,
                strokeWidth = 2f
            )


            Spacer(modifier = Modifier.size(8.dp))


            DashedLine(
                color = Color.Black,
                dashWidth = 6f,
                dashGap = 6f,
                strokeWidth = 2f
            )

            Spacer(modifier = Modifier.size(8.dp))

            NoteInput(note = "", onNoteChange = {})
        }
    }
}


@Composable
fun NoteInput(modifier: Modifier = Modifier, note: String, onNoteChange: (String) -> Unit) {
    var note by remember { mutableStateOf("") }
    val onNoteChange: (String) -> Unit = { it -> note = it }

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

@Composable
@Preview
fun AIGenScreenPreview() {
    AIGenScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    )
}