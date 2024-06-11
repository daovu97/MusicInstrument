package com.musical.instrument.simulator.app.screens.record

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.Converter
import com.musical.instrument.simulator.app.base.compatColor

class WaveformView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paint = Paint()
    private var amplitudes = ArrayList<Float>()
    private var spikes = ArrayList<RectF>()

    private var radius = 6f
    private var w = 9f
    private var d = 6f

    private var sw = 0f
    private var sh = 400f

    private var maxSpikes = 0

    init {
        paint.color = context?.compatColor(R.color.primaryColor) ?: Color.YELLOW
        sw = resources.displayMetrics.widthPixels.toFloat()
        sh = Converter.asPixels(200).toFloat()
        w = Converter.asPixels(5).toFloat()
        d = Converter.asPixels(3).toFloat()
        maxSpikes = (sw / (w + d)).toInt()
    }

    fun addAmplitude(amp: Float) {
        val norm = (amp.toInt() / 7).coerceAtMost(sh.toInt()).toFloat()
        amplitudes.add(norm)

        spikes.clear()

        val amps = amplitudes.takeLast(maxSpikes)

        for (i in amps.indices) {

            val left = sw - i * (w + d)
            val top = sh / 2 - amps[i] / 2
            val right = left + w
            val bottom = top + amps[i]
            spikes.add(RectF(left, top, right, bottom))
        }


        invalidate()
    }

    fun clear(): ArrayList<Float> {
        val amps = amplitudes.clone() as ArrayList<Float>
        amplitudes.clear()
        spikes.clear()
        invalidate()

        return amps
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        spikes.forEach {
            canvas.drawRoundRect(it, radius, radius, paint)
        }
    }
}