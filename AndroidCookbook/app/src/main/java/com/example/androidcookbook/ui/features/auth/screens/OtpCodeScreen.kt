package com.example.androidcookbook.ui.features.auth.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androidcookbook.ui.features.auth.components.ClickableSeparatedText
import com.example.androidcookbook.ui.features.auth.components.InputField
import com.example.androidcookbook.ui.features.auth.components.SignButton
import com.example.androidcookbook.ui.features.auth.components.SignLayout

@Composable
fun OtpCodeScreen(
    otpCode: String,
    onOtpCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onGoBack: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onNavigateToEmail: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SignLayout {
        //TODO: Otp 6 digits field
        Spacer(Modifier.height(15.dp))
        InputField(
            text = otpCode,
            onChange = {
                if (it.length <= 6) {
                    onOtpCodeChange(it)
                }
            },
            placeholderText = "Otp Code",
            type = KeyboardType.Number,
            imeAction = ImeAction.Done,
            onDone = onSubmit
        )
        Spacer(Modifier.height(5.dp))
        //TODO: Submit button
        SignButton(
            onClick = onSubmit,
            actionText = "Continue"
        )

        //TODO: ClickableText("Input the wrong email? Go back")
        ClickableSeparatedText(
            unclickableText = "Input the wrong email? ",
            clickableText = "Go back",
            onClick = onNavigateToEmail
        )

        //TODO: ClickableText("Return to Sign In")
        ClickableSeparatedText(
            unclickableText = "Return to ",
            clickableText = "Sign In",
            onClick = onNavigateToSignIn
        )
    }
}