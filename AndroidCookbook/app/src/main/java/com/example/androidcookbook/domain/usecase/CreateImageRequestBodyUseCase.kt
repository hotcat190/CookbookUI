package com.example.androidcookbook.domain.usecase

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun createImageRequestBody(context: Context, imageUri: Uri): MultipartBody.Part? {
    val file = getFileFromUri(context, imageUri)
    return file?.let {
        val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
        MultipartBody.Part.createFormData("image", it.name, requestFile)
    }
}

fun getFileFromUri(context: Context, uri: Uri?): File? {
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