package com.frankegan.symbiotic

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.util.*

fun Activity.photoOutputUri(
    filePath: String = Environment.DIRECTORY_DCIM
) = when (Build.VERSION.SDK_INT) {
    in Build.VERSION_CODES.LOLLIPOP..Build.VERSION_CODES.P -> legacyFileUri(filePath)
    else -> mediaStoreUri(filePath)
}

fun Fragment.photoOutputUri(
    filePath: String = Environment.DIRECTORY_DCIM
) = when (Build.VERSION.SDK_INT) {
    in Build.VERSION_CODES.LOLLIPOP..Build.VERSION_CODES.P -> requireActivity().legacyFileUri(
        filePath
    )
    else -> requireActivity().mediaStoreUri(filePath)
}

private fun Activity.legacyFileUri(filePath: String): Uri? {
    val filename = UUID.randomUUID().toString()
    val storageDir = Environment
        .getExternalStoragePublicDirectory(filePath)
        .apply { mkdirs() }

    val photoFile: File = try {
        File.createTempFile(filename, ".jpg", storageDir)
    } catch (ex: IOException) {
        Log.e("#legacyFileUri", ex.message, ex)
        return null
    }

    return FileProvider.getUriForFile(this, "${application.packageName}.fileprovider", photoFile)
}

@TargetApi(Build.VERSION_CODES.Q)
private fun Activity.mediaStoreUri(filePath: String): Uri? {
    val filename: String = UUID.randomUUID().toString()
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.TITLE, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, filePath)
    }
    return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
}
