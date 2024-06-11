package com.musical.instrument.simulator.app.screens.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Utils {

    const val RECORD_AUDIO_PERMISSION_CODE: Int = 79
    const val STORAGE_PERMISSION_CODE: Int = 2003

    fun requestPermissionRecordAudio(activity: AppCompatActivity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(
                Manifest.permission.RECORD_AUDIO
            ), RECORD_AUDIO_PERMISSION_CODE
        )
    }

    fun requestPermissionStorage(
        context: Context,
        activity: AppCompatActivity,
        storageActivityResultLauncher: ActivityResultLauncher<String>
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_MEDIA_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                storageActivityResultLauncher.launch(Manifest.permission.READ_MEDIA_AUDIO)
            }

        } else {
            ActivityCompat.requestPermissions(
                activity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                ), STORAGE_PERMISSION_CODE
            )
        }
    }

    fun checkPermissionStorage(
        context: Context, image: Switch
    ): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            (ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED)
            image.isChecked = true
            image.isEnabled = false
            return true
        } else {
            if ((ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    context, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED)
            ) {
                image.isEnabled = false
                image.isChecked = true
                return true
            } else {
                image.isEnabled = true
                image.isChecked = false
                return false
            }
        }
    }

    fun checkPermissionRecordAudio(context: Context, image: Switch): Boolean {
        return if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            image.isChecked = true
            image.isEnabled = false
            true
        } else {
            image.isActivated = false
            image.isChecked = false
            false
        }
    }
}