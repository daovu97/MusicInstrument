package com.musical.instrument.simulator.app.screens.theme

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.base.Settings
import com.musical.instrument.simulator.app.base.setOnBackClick
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.ActivityThemesBinding
import com.musical.instrument.simulator.app.utils.AdsInter
import com.musical.instrument.simulator.app.utils.AdsNative
import com.musical.instrument.simulator.app.utils.OnSwipeTouchListener
import dagger.hilt.android.AndroidEntryPoint

data class ThemeBackground(
    val id: Int, @DrawableRes val source: Int? = null, val url: String? = null
) {
    companion object {

        fun get(id: Int): ThemeBackground = allTheme.firstOrNull { it.id == id } ?: allTheme.first()

        val current: ThemeBackground
            get() = get(Settings.CURRENT_THEME_BG)
        val allTheme: List<ThemeBackground>
            get() = listOf(
                ThemeBackground(0, R.drawable.bg_1),
                ThemeBackground(1, R.drawable.bg_2),
                ThemeBackground(2, R.drawable.bg_3),
                ThemeBackground(3, R.drawable.bg_4),
                ThemeBackground(4, R.drawable.bg_5),
                ThemeBackground(5, R.drawable.bg_6),
                ThemeBackground(6, R.drawable.bg_7),
                ThemeBackground(7, R.drawable.bg_8),
            )
    }
}

@AndroidEntryPoint
class ThemeActivity : BaseActivity<ActivityThemesBinding>(ActivityThemesBinding::inflate) {

    private var currentSelectedID: Int = ThemeBackground.current.id
    private var listBackground = ThemeBackground.allTheme
    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
        loadAdsNativeTheme()
        AdsInter.THEME.load()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.btnAccept.setOnSingleClickListener {
            Settings.CURRENT_THEME_BG = currentSelectedID
            AdsInter.THEME.showInterAds(this,
                onNextAction = {
                    finish()
                },
                onFail = { finish() })
        }

        val swipeHandle = object : OnSwipeTouchListener(this) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Log.d("daovu", "onSwipeLeft")
                if (currentSelectedID >= listBackground.map { it.id }.max()) return
                currentSelectedID++
                changeBackground(binding.background, ThemeBackground.get(currentSelectedID))
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                Log.d("daovu", "onSwipeRight")
                if (currentSelectedID <= 0) return
                currentSelectedID--
                changeBackground(binding.background, ThemeBackground.get(currentSelectedID))
            }
        }

        binding.btnLeft.setOnSingleClickListener {
            swipeHandle.onSwipeLeft()
        }

        binding.btnRight.setOnSingleClickListener {
            swipeHandle.onSwipeRight()
        }

        binding.root.setOnTouchListener(swipeHandle)
    }

    private fun onClick() {
        binding.header.setOnBackClick {
            finish()
        }
    }

    private fun loadAdsNativeTheme() {
        AdsNative.THEME.loadAds(this, R.layout.ads_native_small, binding.frNativeAds)
    }
}