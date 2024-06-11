package com.musical.instrument.simulator.app.screens.guitar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.media.MediaPlayer
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.model.Guitar

object Utils {
    const val flagAm = 0
    const val flagC = 1
    const val flagB = 2
    const val flagE = 3
    const val flagDm = 4
    const val flagF = 5
    const val flagEm = 6
    const val flagG = 7

    fun loadAnimation(image: ImageView) {
        val upAnimator =
            ObjectAnimator.ofFloat(image, "translationY", 0f, -15f)
        upAnimator.duration = 25
        upAnimator.interpolator = AccelerateDecelerateInterpolator()
        val downAnimator =
            ObjectAnimator.ofFloat(image, "translationY", -15f, 0f)
        downAnimator.duration = 25
        downAnimator.interpolator = AccelerateDecelerateInterpolator()
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(upAnimator, downAnimator)
        animatorSet.duration = 50
        animatorSet.start()
    }

    fun getGuitar(): MutableList<Guitar> {
        val list: MutableList<Guitar> = mutableListOf()

        list.add(Guitar(flagAm, R.drawable.state_button_am, false))
        list.add(Guitar(flagC, R.drawable.state_button_c, false))
        list.add(Guitar(flagB, R.drawable.state_button_bm, false))
        list.add(Guitar(flagE, R.drawable.state_button_e, false))
        list.add(Guitar(flagDm, R.drawable.state_button_dm, false))
        list.add(Guitar(flagF, R.drawable.state_button_f, false))
        list.add(Guitar(flagEm, R.drawable.state_button_em, false))
        list.add(Guitar(flagG, R.drawable.state_button_g, false))

        return list
    }
}