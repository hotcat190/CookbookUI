package com.example.androidcookbook.ui.nav

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.example.androidcookbook.domain.model.post.Post
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavTypes {
    val PostType = object : NavType<Post>(
        isNullableAllowed = false
    ) {
        override fun get(bundle: Bundle, key: String): Post? {
            return Json.decodeFromString(bundle.getString(key) ?: return null)
        }

        override fun parseValue(value: String): Post {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: Post): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: Post) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}