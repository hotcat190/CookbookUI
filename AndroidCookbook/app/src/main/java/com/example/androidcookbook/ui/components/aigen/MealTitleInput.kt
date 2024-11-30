package com.example.androidcookbook.ui.components.aigen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidcookbook.R

@Composable
fun MealTitleInput(
    modifier: Modifier = Modifier,
    mealTitle: String,
    onMealTitleChange: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 4.dp)) {
        AiGenInputLabel(
            imageResource = R.drawable.meal_icon, title = "Dish title",
            contentDescription = "Meal Icon"
        )
        Spacer(Modifier.size(1.dp))
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = mealTitle,
            onValueChange = onMealTitleChange,
            placeholder = {
                Text(
                    "Insert your dish title here...",

                    fontFamily = FontFamily(Font(R.font.nunito_regular)),
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFFFFF).copy(alpha = 0.75f),
                    fontSize = 14.sp

                )
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                textColor = Color.White,
                backgroundColor = Color(0xFF4A4A4A)
            ),
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
            )
    }
}