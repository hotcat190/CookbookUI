package com.example.androidcookbook

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.androidcookbook.ui.CookbookApp
import com.example.androidcookbook.ui.theme.AndroidCookbookTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCm registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result

            Log.d("FCM", token.toString())
        })



        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            AndroidCookbookTheme {
                CookbookApp()
            }
        }
    }
}
