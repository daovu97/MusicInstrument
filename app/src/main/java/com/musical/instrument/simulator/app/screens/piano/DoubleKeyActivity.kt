package com.musical.instrument.simulator.app.screens.piano

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.chengtao.pianoview.entity.Piano
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityDoubleKeyBinding
import com.musical.instrument.simulator.app.model.MyPiano

class DoubleKeyActivity : BaseActivity<ActivityDoubleKeyBinding>(ActivityDoubleKeyBinding::inflate),
    OnClickListener {

    private lateinit var mPiano: MyPiano

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
        onChange()
    }


    private fun init() {
        mPiano = Utils.getPianoThemes()[Utils.getPianoThemes(this@DoubleKeyActivity)]
        binding.apply {
            background1.setImageResource(mPiano.background!!)
            background2.setImageResource(mPiano.background!!)
            imgStart3.setImageResource(mPiano.buttonStart!!)
            imgPrevious3.setImageResource(mPiano.buttonPrevious!!)
            imgNext3.setImageResource(mPiano.buttonNext!!)
            imgEnd3.setImageResource(mPiano.buttonEnd!!)
            Piano.whiteDrawable = mPiano.whiteKey!!
            Piano.blackDrawable = mPiano.blackKey!!
            imgStart2.setImageResource(mPiano.buttonStart!!)
            imgPrevious2.setImageResource(mPiano.buttonNext!!)
            imgNext2.setImageResource(mPiano.buttonNext!!)
            imgEnd2.setImageResource(mPiano.buttonEnd!!)
        }
    }

    private fun onClick() {
        binding.iconBack.setOnClickListener(this)
        binding.imgStart3.setOnClickListener(this)
        binding.imgEnd3.setOnClickListener(this)
        binding.imgStart2.setOnClickListener(this)
        binding.imgEnd2.setOnClickListener(this)
        binding.imgPrevious3.setOnClickListener(this)
        binding.imgNext3.setOnClickListener(this)
        binding.imgPrevious2.setOnClickListener(this)
        binding.imgNext2.setOnClickListener(this)
    }

    private fun onChange() {
        binding.seekbar3.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.pianoView1.scroll(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        binding.seekbar2.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.pianoView2.scroll(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.iconBack.id -> {
                finish()
            }

            binding.imgStart3.id -> {
                binding.seekbar3.progress -= 10
            }

            binding.imgEnd3.id -> {
                binding.seekbar3.progress += 10
            }

            binding.imgStart2.id -> {
                binding.seekbar2.progress -= 10
            }

            binding.imgEnd2.id -> {
                binding.seekbar2.progress += 10
            }
        }
    }

}