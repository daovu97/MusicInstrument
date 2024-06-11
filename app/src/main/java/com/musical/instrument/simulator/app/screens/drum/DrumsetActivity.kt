package com.musical.instrument.simulator.app.screens.drum

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityDrumeBinding

class DrumsetActivity : BaseActivity<ActivityDrumeBinding>(ActivityDrumeBinding::inflate) {
    private var soundPool: SoundPool? = null
    private var soundCrashCenter: Int? = null
    private var soundKick: Int? = null
    private var soundSnare: Int? = null
    private var soundDrumSmall: Int? = null
    private var soundRealistic: Int? = null
    private var soundDrumBig: Int? = null
    private var soundTom: Int? = null
    private var soundFloor: Int? = null

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
    }

    private fun init() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes: AudioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder().setMaxStreams(30).setAudioAttributes(audioAttributes).build()
        } else {
            SoundPool(30, AudioManager.STREAM_MUSIC, 0)
        }
        soundCrashCenter = soundPool?.load(this@DrumsetActivity, R.raw.drum_center, 1)
        soundKick = soundPool?.load(this@DrumsetActivity, R.raw.kick, 1)
        soundSnare = soundPool?.load(this@DrumsetActivity, R.raw.snare, 1)
        soundDrumSmall = soundPool?.load(this@DrumsetActivity, R.raw.drum_center, 1)
        soundRealistic = soundPool?.load(this@DrumsetActivity, R.raw.drum_small, 1)
        soundDrumBig = soundPool?.load(this@DrumsetActivity, R.raw.crash, 1)
        soundTom = soundPool?.load(this@DrumsetActivity, R.raw.tom, 1)
        soundFloor = soundPool?.load(this@DrumsetActivity, R.raw.tom_floor, 1)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.imageBack.setOnClickListener {
            finish()
        }

        binding.imageDrumBigCenter.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundCrashCenter!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageDrumBigCenter)
                    true
                }

                else -> false
            }
        }
        binding.imageKickLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundKick!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageKickLeft)
                    true
                }

                else -> false
            }
        }
        binding.imageKickRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundKick!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageKickRight)
                    true
                }

                else -> false
            }
        }
        binding.imageSnare.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundSnare!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageSnare)
                    true
                }

                else -> false
            }
        }
        binding.imageDrumSmallRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundDrumSmall!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageDrumSmallRight)
                    true
                }

                else -> false
            }
        }
        binding.imageDrumSmallLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundDrumSmall!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageDrumSmallLeft)
                    true
                }

                else -> false
            }
        }
        binding.imageRealistic.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundRealistic!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageRealistic)
                    true
                }

                else -> false
            }
        }
        binding.imageDrumBigRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundDrumBig!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageDrumBigRight)
                    true
                }

                else -> false
            }
        }
        binding.imageDrumBigLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundDrumBig!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageDrumBigLeft)
                    true
                }

                else -> false
            }
        }
        binding.imageTom.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundTom!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageTom)
                    true
                }

                else -> false
            }
        }
        binding.imageFloorTomLeft.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundFloor!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageFloorTomLeft)
                    true
                }

                else -> false
            }
        }
        binding.imageFloorTomRight.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    soundPool?.play(soundFloor!!, 1f, 1f, 0, 0, 1f)
                    Utils.animScaleDrum(binding.imageFloorTomRight)
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroy() {
        soundPool?.release()
        super.onDestroy()
    }
}