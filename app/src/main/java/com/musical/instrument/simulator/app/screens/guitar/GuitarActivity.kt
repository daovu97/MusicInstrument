package com.musical.instrument.simulator.app.screens.guitar

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.GutiarAdapter
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityGuitarBinding
import com.musical.instrument.simulator.app.model.Guitar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GuitarActivity : BaseActivity<ActivityGuitarBinding>(ActivityGuitarBinding::inflate) {
    private var flag: Int = -1
    private lateinit var pianoAdapter: GutiarAdapter
    private lateinit var guitars: MutableList<Guitar>
    private lateinit var images: MutableList<ImageView>
    private var soundPool: SoundPool? = null

    private var soundF1: Int? = null
    private var soundG1: Int? = null
    private var soundE11: Int? = null
    private var soundA12: Int? = null
    private var soundF2: Int? = null
    private var soundE2: Int? = null
    private var soundC2: Int? = null
    private var soundA3: Int? = null
    private var soundF3: Int? = null
    private var soundBm3: Int? = null
    private var soundD23: Int? = null
    private var soundEm3: Int? = null
    private var soundA4: Int? = null
    private var soundG24: Int? = null
    private var soundBm4: Int? = null
    private var soundE4: Int? = null
    private var soundDm4: Int? = null
    private var soundF4: Int? = null
    private var soundA5: Int? = null
    private var soundB5: Int? = null
    private var soundB25: Int? = null
    private var soundDm5: Int? = null
    private var soundF5: Int? = null
    private var soundE36: Int? = null
    private var soundBm6: Int? = null
    private var soundDm6: Int? = null
    private var soundF6: Int? = null
    private var soundG6: Int? = null

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onTouch()
        onClick()
    }

    private fun init() {
        makeSoundPool()
        initNote()
        images = mutableListOf()
        guitars = mutableListOf()
        guitars = Utils.getGuitar()
        pianoAdapter =
            GutiarAdapter(this@GuitarActivity, guitars, object : GutiarAdapter.ClickItem {
                override fun chooseButton(item: Guitar, index: Int) {
                    handleClickButton(item, index)
                }
            })
        binding.rcv.apply {
            layoutManager = GridLayoutManager(this@GuitarActivity, 2)
            adapter = pianoAdapter
        }

        binding.lineGuitar1.isActivated = true
        binding.lineGuitar2.isActivated = true

        getListImage()
    }

    private fun makeSoundPool() {
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes: AudioAttributes =
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
            SoundPool.Builder().setMaxStreams(10).setAudioAttributes(audioAttributes).build()
        } else {
            SoundPool(10, AudioManager.STREAM_MUSIC, 0)
        }
    }

    private fun initNote() {
        soundF1 = soundPool?.load(this@GuitarActivity, R.raw.f_1, 0)
        soundG1 = soundPool?.load(this@GuitarActivity, R.raw.g_1, 0)
        soundE11 = soundPool?.load(this@GuitarActivity, R.raw.e1_1, 0)

        soundA12 = soundPool?.load(this@GuitarActivity, R.raw.a1_2, 0)
        soundF2 = soundPool?.load(this@GuitarActivity, R.raw.f_2, 0)
        soundE2 = soundPool?.load(this@GuitarActivity, R.raw.e_2, 0)
        soundC2 = soundPool?.load(this@GuitarActivity, R.raw.c_2, 0)

        soundA3 = soundPool?.load(this@GuitarActivity, R.raw.am_3, 0)
        soundF3 = soundPool?.load(this@GuitarActivity, R.raw.f_3, 0)
        soundBm3 = soundPool?.load(this@GuitarActivity, R.raw.bm_3, 0)
        soundD23 = soundPool?.load(this@GuitarActivity, R.raw.d2_3, 0)
        soundEm3 = soundPool?.load(this@GuitarActivity, R.raw.em_3, 0)

        soundA4 = soundPool?.load(this@GuitarActivity, R.raw.am_4, 0)
        soundBm4 = soundPool?.load(this@GuitarActivity, R.raw.bm_4, 0)
        soundG24 = soundPool?.load(this@GuitarActivity, R.raw.g2_4, 0)
        soundE4 = soundPool?.load(this@GuitarActivity, R.raw.e_4, 0)
        soundDm4 = soundPool?.load(this@GuitarActivity, R.raw.dm_4, 0)
        soundF4 = soundPool?.load(this@GuitarActivity, R.raw.f_4, 0)

        soundA5 = soundPool?.load(this@GuitarActivity, R.raw.am_5, 0)
        soundB5 = soundPool?.load(this@GuitarActivity, R.raw.bm_5, 0)
        soundB25 = soundPool?.load(this@GuitarActivity, R.raw.b2_5, 0)
        soundDm5 = soundPool?.load(this@GuitarActivity, R.raw.dm_5, 0)
        soundF5 = soundPool?.load(this@GuitarActivity, R.raw.f_5, 0)

        soundE36 = soundPool?.load(this@GuitarActivity, R.raw.e3_6, 0)
        soundBm6 = soundPool?.load(this@GuitarActivity, R.raw.bm_6, 0)
        soundDm6 = soundPool?.load(this@GuitarActivity, R.raw.dm_6, 0)
        soundF6 = soundPool?.load(this@GuitarActivity, R.raw.f_6, 0)
        soundG6 = soundPool?.load(this@GuitarActivity, R.raw.g_6, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onTouch() {
        binding.lineGuitar1.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar1)
                    when (flag) {
                        Utils.flagAm -> {
                        }

                        Utils.flagB -> {
                        }

                        Utils.flagC -> {
                        }

                        Utils.flagDm -> {
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundF1!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundG1!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundE11!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        binding.lineGuitar2.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar2)
                    when (flag) {
                        Utils.flagAm -> {
                            soundPool?.play(soundA12!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagC -> {
                            soundPool?.play(soundF2!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagB -> {
                            soundPool?.play(soundE2!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagE -> {
                            soundPool?.play(soundE2!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagDm -> {
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundC2!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagEm -> {
                            soundPool?.play(soundE2!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundE2!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundA12!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        binding.lineGuitar3.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar3)
                    when (flag) {
                        Utils.flagAm -> {
                            soundPool?.play(soundA3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagC -> {
                            soundPool?.play(soundF3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagB -> {
                            soundPool?.play(soundBm3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagE -> {
                            soundPool?.play(soundF3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagDm -> {
                            soundPool?.play(soundD23!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundF3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagEm -> {
                            soundPool?.play(soundEm3!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundD23!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundD23!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        binding.lineGuitar4.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar4)
                    when (flag) {
                        Utils.flagAm -> {
                            soundPool?.play(soundA4!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagC -> {
                            soundPool?.play(soundG24!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagB -> {
                            soundPool?.play(soundBm4!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagE -> {
                            soundPool?.play(soundE4!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagDm -> {
                            soundPool?.play(soundDm4!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundF4!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagEm -> {
                            soundPool?.play(soundG24!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundG24!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundG24!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        binding.lineGuitar5.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar5)
                    when (flag) {
                        Utils.flagAm -> {
                            soundPool?.play(soundA5!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagC -> {
                            soundPool?.play(soundA5!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagB -> {
                            soundPool?.play(soundB5!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagE -> {
                            soundPool?.play(soundB25!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagDm -> {
                            soundPool?.play(soundDm5!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundF5!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagEm -> {
                            soundPool?.play(soundB25!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundB25!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundB25!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }

        binding.lineGuitar6.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Utils.loadAnimation(binding.lineGuitar6)
                    when (flag) {
                        Utils.flagAm -> {
                            soundPool?.play(soundE36!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagC -> {
                            soundPool?.play(soundE36!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagB -> {
                            soundPool?.play(soundDm6!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagE -> {
                            soundPool?.play(soundE36!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagDm -> {
                            soundPool?.play(soundDm6!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagF -> {
                            soundPool?.play(soundF6!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagEm -> {
                            soundPool?.play(soundE36!!, 1f, 1f, 0, 0, 1f)
                        }

                        Utils.flagG -> {
                            soundPool?.play(soundG6!!, 1f, 1f, 0, 0, 1f)
                        }

                        else -> {
                            soundPool?.play(soundE36!!, 1f, 1f, 0, 0, 1f)
                        }
                    }
                    true
                }

                else -> false
            }
        }
    }

    private fun onClick() {
        binding.icBack.setOnClickListener {
            finish()
        }
    }

    private fun handleClickButton(item: Guitar, index: Int) {
        val time1 = System.currentTimeMillis()
        guitars[index].isSelected = !guitars[index].isSelected!!
        flag = if (guitars[index].isSelected!!) {
            item.id!!
        } else {
            -1
        }
        for (i in 0 until guitars.size) {
            if (i != index) {
                guitars[i].isSelected = false
            }
        }
        pianoAdapter.setListGuitar(guitars)
        val time2 = System.currentTimeMillis()
        Log.d("Check_Time_Loop", "${time2 - time1}ms")
        hideImage()

        when (flag) {
            Utils.flagAm -> {
                binding.buttonAm1.visibility = View.VISIBLE
                binding.buttonAm2.visibility = View.VISIBLE
                binding.buttonAm3.visibility = View.VISIBLE
                disableLine1()
            }

            Utils.flagC -> {
                binding.buttonC1.visibility = View.VISIBLE
                binding.buttonC3.visibility = View.VISIBLE
                binding.buttonC2.visibility = View.VISIBLE
                disableLine1()
            }

            Utils.flagB -> {
                disableLine1()
            }

            Utils.flagDm -> {
                binding.buttonDm1.visibility = View.VISIBLE
                binding.buttonDm2.visibility = View.VISIBLE
                binding.buttonDm3.visibility = View.VISIBLE
                binding.buttonDm4.visibility = View.VISIBLE
                disableLine1And2()
            }

            Utils.flagG -> {
                binding.buttonG1.visibility = View.VISIBLE
                binding.buttonG2.visibility = View.VISIBLE
                binding.buttonG3.visibility = View.VISIBLE
                enableAllLine()
            }

            Utils.flagF -> {
                binding.buttonF1.visibility = View.VISIBLE
                binding.buttonF2.visibility = View.VISIBLE
                binding.buttonF3.visibility = View.VISIBLE
                binding.buttonF4.visibility = View.VISIBLE
            }

            else -> {
                enableAllLine()
            }
        }
    }

    private fun disableLine1() {
        binding.lineGuitar1.isActivated = false
        binding.lineGuitar2.isActivated = true

        binding.iconDisable1.visibility = View.VISIBLE
    }

    private fun disableLine1And2() {
        binding.lineGuitar1.isActivated = false
        binding.lineGuitar2.isActivated = false

        binding.iconDisable1.visibility = View.VISIBLE
        binding.iconDisable2.visibility = View.VISIBLE
    }

    private fun enableAllLine() {
        binding.lineGuitar1.isActivated = true
        binding.lineGuitar2.isActivated = true
        binding.iconDisable1.visibility = View.INVISIBLE
        binding.iconDisable2.visibility = View.INVISIBLE
    }

    private fun getListImage() {
        images.add(binding.iconDisable2)
        images.add(binding.iconDisable1)
        images.add(binding.buttonAm1)
        images.add(binding.buttonAm2)
        images.add(binding.buttonAm3)
        images.add(binding.buttonC1)
        images.add(binding.buttonC2)
        images.add(binding.buttonC3)
        images.add(binding.buttonDm1)
        images.add(binding.buttonDm2)
        images.add(binding.buttonDm3)
        images.add(binding.buttonDm4)
        images.add(binding.buttonG1)
        images.add(binding.buttonG2)
        images.add(binding.buttonG3)
        images.add(binding.buttonF1)
        images.add(binding.buttonF2)
        images.add(binding.buttonF3)
        images.add(binding.buttonF4)
    }

    private fun hideImage() {
        for (item in images) {
            item.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool!!.release()
    }
}