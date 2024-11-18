package com.example.androidcookbook.ui.features.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ClickableSeparatedText(
    unclickableText: String,
    clickableText: String,
    onClick: () -> Unit
) {
    Row {
        Text(
            text = unclickableText,
            color = Color.White,
            fontWeight = FontWeight(600)
        )
        Text(
            modifier = Modifier.clickable {
                onClick()
            },
            text = clickableText,
            color = Color(134, 147, 95),
            fontWeight = FontWeight(600)
        )
    }
}

@Preview
@Composable
fun TextPreview() {
    ClickableSeparatedText(
        unclickableText = "Doesn’t have account ?",
        clickableText = "Sign Up"
    ){}
}