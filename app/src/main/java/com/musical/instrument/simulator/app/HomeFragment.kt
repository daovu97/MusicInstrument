package com.musical.instrument.simulator.app

import android.annotation.SuppressLint
import android.content.Intent
import com.musical.instrument.simulator.app.base.BaseFragment
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.ActivityHomeBinding
import com.musical.instrument.simulator.app.screens.drum.DrumsetActivity
import com.musical.instrument.simulator.app.screens.guitar.GuitarActivity
import com.musical.instrument.simulator.app.screens.piano.PianoActivity
import com.musical.instrument.simulator.app.screens.record.RecordViActivity
import com.musical.instrument.simulator.app.utils.AdsInter
import com.musical.instrument.simulator.app.utils.AdsNative
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initView() {
        AdsInter.PIANO.load()
        onClick()

        AdsNative.HOME.loadAds(activity, R.layout.ads_native_small, binding.frNativeAds)
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun onClick() {
        binding.layoutPiano.setOnSingleClickListener {
            val activity = activity ?: return@setOnSingleClickListener
            AdsInter.PIANO.showInterAds(activity, onNextAction = {
                startActivity(Intent(activity, PianoActivity::class.java))
            }) {
                startActivity(Intent(activity, PianoActivity::class.java))
            }
        }

        binding.layoutGuitar.setOnSingleClickListener {
            startActivity(Intent(activity, GuitarActivity::class.java))
        }

        binding.layoutDrumset.setOnSingleClickListener {
            startActivity(Intent(activity, DrumsetActivity::class.java))
        }

        binding.layoutMicro.setOnSingleClickListener {
            startActivity(Intent(activity, RecordViActivity::class.java))
        }
    }
}