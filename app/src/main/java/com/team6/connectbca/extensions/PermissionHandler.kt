package com.team6.connectbca.extensions

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

fun checkPermissionLogic(context: Context) : Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (isMediaImagePermissionGranted(context) &&
                isCameraPermissionGranted(context))
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        (isCameraPermissionGranted(context) &&
                isReadExternalStoragePermissionGranted(context))
    } else {
        (isCameraPermissionGranted(context) &&
                isWriteExternalStoragePermissionGranted(context))
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun isMediaImagePermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PERMISSION_GRANTED
}

private fun isWriteExternalStoragePermissionGranted(context: Context) : Boolean {
    return ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED
}

private fun isReadExternalStoragePermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED
}

private fun isCameraPermissionGranted(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, CAMERA) == PERMISSION_GRANTED
}