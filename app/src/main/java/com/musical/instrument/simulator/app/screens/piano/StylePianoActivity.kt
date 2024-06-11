package com.musical.instrument.simulator.app.screens.piano

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.ThemePianoAdapter
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityStylePianoBinding
import com.musical.instrument.simulator.app.model.MyPiano
import com.musical.instrument.simulator.app.model.ThemesPiano
import com.musical.instrument.simulator.app.utils.AdsInter
import com.nlbn.ads.callback.InterCallback
import com.nlbn.ads.util.Admob

class StylePianoActivity :
    BaseActivity<ActivityStylePianoBinding>(ActivityStylePianoBinding::inflate),
    OnClickListener {

    private lateinit var themePianos: MutableList<ThemesPiano>
    private lateinit var pianoAdapter: ThemePianoAdapter
    private lateinit var mPiano: MyPiano
    private var flag: Int = Utils.PIANO_THEME_DEFAULT

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
        loadAdsInterStyle()
    }

    private fun init() {
        themePianos = mutableListOf()
        themePianos = Utils.getThemesPiano()
        pianoAdapter =
            ThemePianoAdapter(this@StylePianoActivity, themePianos, binding.layoutContainer)
        binding.layoutContainer.apply {
            adapter = pianoAdapter
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val transformer = CompositePageTransformer()
            transformer.addTransformer(MarginPageTransformer(0))
            transformer.addTransformer { page, postion ->
                page.scaleY = 0.85f + 0.14f
            }
            setPageTransformer(transformer)
        }
        binding.circleIndicator.setViewPager(binding.layoutContainer)
        mPiano = Utils.getPianoThemes()[Utils.getPianoThemes(this@StylePianoActivity)]
        flag = mPiano.id!!
        when (mPiano.id!!) {
            0 -> {
                binding.viewSupportedLeft.visibility = View.GONE
                binding.viewSupportedRight.visibility = View.VISIBLE
            }

            themePianos.size - 1 -> {
                binding.viewSupportedLeft.visibility = View.VISIBLE
                binding.viewSupportedRight.visibility = View.GONE
            }

            else -> {
                binding.viewSupportedLeft.visibility = View.VISIBLE
                binding.viewSupportedRight.visibility = View.VISIBLE
            }
        }
        binding.layoutContainer.currentItem = mPiano.id!!
        binding.layoutContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                flag = position
                when (position) {
                    0 -> {
                        binding.viewSupportedLeft.visibility = View.GONE
                        binding.viewSupportedRight.visibility = View.VISIBLE
                    }

                    themePianos.size - 1 -> {
                        binding.viewSupportedLeft.visibility = View.VISIBLE
                        binding.viewSupportedRight.visibility = View.GONE
                    }

                    else -> {
                        binding.viewSupportedLeft.visibility = View.VISIBLE
                        binding.viewSupportedRight.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun onClick() {
        binding.icBack.setOnClickListener(this)
        binding.iconDone.setOnClickListener(this)
    }

    private fun loadAdsInterStyle() {
        if (AdsInter.inter_style == null) {
            Admob.getInstance()
                .loadInterAds(
                    this,
                    getString(R.string.inter_style),
                    object : InterCallback() {
                        override fun onInterstitialLoad(interstitialAd: InterstitialAd) {
                            super.onInterstitialLoad(interstitialAd)
                            AdsInter.inter_style = interstitialAd
                        }
                    })
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.icBack.id -> {
                finish()
            }

            binding.iconDone.id -> {

//                if (haveNetworkConnection(this)) {
//                    Admob.getInstance()
//                        .showInterAds(
//                            this@StylePianoActivity,
//                            AdsInter.inter_style,
//                            object : InterCallback() {
//                                override fun onNextAction() {
//                                    super.onNextAction()
//
//                                    Utils.savePianoThemes(this@StylePianoActivity, flag)
//                                    val intent =
//                                        Intent(this@StylePianoActivity, PianoActivity::class.java)
//                                    intent.putExtra(Utils.KEY_BACK_SCREEN, Utils.VALUE_BACK_SCREEN)
//                                    startActivity(intent)
//                                    finishAffinity()
//
//                                    AdsInter.inter_style = null
//
//                                    loadAdsInterStyle()
//                                }
//                            })
//                } else {
//                    Utils.savePianoThemes(this@StylePianoActivity, flag)
//                    val intent = Intent(this@StylePianoActivity, PianoActivity::class.java)
//                    intent.putExtra(Utils.KEY_BACK_SCREEN, Utils.VALUE_BACK_SCREEN)
//                    startActivity(intent)
//                    finishAffinity()
//                }

            }
        }
    }
}