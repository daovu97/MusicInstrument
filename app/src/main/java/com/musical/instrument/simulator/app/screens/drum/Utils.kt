package com.musical.instrument.simulator.app.screens.drum

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import com.musical.instrument.simulator.app.R

object Utils {
    private const val SCALE_FACTOR = 1.1f // Tỷ lệ phóng to

    fun animScaleDrum(image: ImageView) {
        val enlargeAnimation = ScaleAnimation(
            1.0f, SCALE_FACTOR, // Tỷ lệ phóng to theo trục X
            1.0f, SCALE_FACTOR, // Tỷ lệ phóng to theo trục Y
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (điểm neo) là giữa ImageView
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (điểm neo) là giữa ImageView
        ).apply {
            duration = 100 // Thời gian phóng to (100ms)
            fillAfter = true // Giữ lại trạng thái sau khi hoàn thành animation
        }

        val restoreAnimation = ScaleAnimation(
            SCALE_FACTOR, 1.0f, // Tỷ lệ thu nhỏ theo trục X
            SCALE_FACTOR, 1.0f, // Tỷ lệ thu nhỏ theo trục Y
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X (điểm neo) là giữa ImageView
            Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y (điểm neo) là giữa ImageView
        ).apply {
            duration = 200 // Thời gian thu nhỏ (200ms)
            fillAfter = true // Giữ lại trạng thái sau khi hoàn thành animation
        }

        image.startAnimation(enlargeAnimation)

        Handler(Looper.getMainLooper()).postDelayed({
            image.startAnimation(restoreAnimation)
        }, 100) // Sau 100ms, bắt đầu animation thu nhỏ
    }
}