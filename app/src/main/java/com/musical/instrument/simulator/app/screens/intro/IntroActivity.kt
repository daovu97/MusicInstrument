package com.musical.instrument.simulator.app.screens.intro

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.IntroAdapter
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityIntroBinding
import com.musical.instrument.simulator.app.screens.main.HomeActivity
import com.musical.instrument.simulator.app.screens.permission.PermissionActivity
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob

class IntroActivity : BaseActivity<ActivityIntroBinding>(ActivityIntroBinding::inflate) {

    private lateinit var introAdapter: IntroAdapter

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
    }

    fun init() {
        introAdapter = IntroAdapter(this@IntroActivity)
        binding.layoutContainer.apply {
            adapter = introAdapter
        }
        binding.circleIndicator.setViewPager(binding.layoutContainer)
        binding.layoutContainer.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
//                binding.textNext.text = when (position) {
//                    0 -> {
//                        setStatusBarGradiant(R.drawable.image_intro_1)
//                        resources.getString(R.string.next)
//                    }
//
//                    1 -> {
//                        setStatusBarGradiant(R.drawable.image_intro_2)
//                        resources.getString(R.string.next)
//                    }
//
//                    2 -> {
//                        setStatusBarGradiant(R.drawable.image_intro_3)
//                        resources.getString(R.string.start)
//                    }
//
//                    else -> {
//                        setStatusBarGradiant(R.drawable.image_intro_1)
//                        resources.getString(R.string.next)
//                    }
//                }
            }
        })

        loadAdsNativeIntro()
    }

    fun onClick() {
        binding.textNext.setOnClickListener {
            var index: Int = binding.layoutContainer.currentItem
            if (index < 2) {
                ++index
                binding.layoutContainer.currentItem = index
            } else {
                if (Utils.getIsFirstInstallApp(this@IntroActivity)
                        .matches(Utils.VALUE_DEFAULT_IS_FIRST_INSTALL_APP.toRegex())
                ) { // first install app
                    val intent = Intent(this@IntroActivity, PermissionActivity::class.java)
                    intent.putExtra("Key_From_Intro_Screen", 79)
                    startActivity(intent)
                    Utils.saveIsFirstInstallApp(this@IntroActivity)
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (checkPermissionRecordAudio()) {
                            startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
                        } else {
                            val intent = Intent(this@IntroActivity, PermissionActivity::class.java)
                            intent.putExtra("Key_From_Intro_Screen", 79)
                            startActivity(intent)
                        }
                    } else {
                        if (checkPermissionStorage() && checkPermissionRecordAudio()) {
                            startActivity(Intent(this@IntroActivity, HomeActivity::class.java))
                        } else {
                            val intent = Intent(this@IntroActivity, PermissionActivity::class.java)
                            intent.putExtra("Key_From_Intro_Screen", 79)
                            startActivity(intent)
                        }
                    }
                }
                finish()
            }
            binding.textNext.text = if (index == 2) {
                resources.getString(R.string.start)
            } else {
                resources.getString(R.string.next)
            }
        }
    }

    private fun checkPermissionStorage(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this@IntroActivity, Manifest.permission.READ_MEDIA_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            (ContextCompat.checkSelfPermission(
                this@IntroActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this@IntroActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(
                this@IntroActivity, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this@IntroActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    private fun checkPermissionRecordAudio(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@IntroActivity, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun loadAdsNativeIntro() {
//        if (haveNetworkConnection(this)) {
//            try {
//                Admob.getInstance().loadNativeAd(
//                    this@IntroActivity,
//                    getString(R.string.native_intro),
//                    object : NativeCallback() {
//                        override fun onNativeAdLoaded(nativeAd: NativeAd) {
//                            val adView: View = LayoutInflater.from(this@IntroActivity)
//                                .inflate(R.layout.ads_native_lang, null)
//                            val nativeAdView = adView as NativeAdView
//                            binding.frNativeAds.removeAllViews()
//                            binding.frNativeAds.addView(adView)
//                            Admob.getInstance().pushAdsToViewCustom(nativeAd, nativeAdView)
//                        }
//
//                        override fun onAdFailedToLoad() {
//                            binding.frNativeAds.removeAllViews()
//                        }
//                    }
//                )
//            } catch (e: Exception) {
//                binding.frNativeAds.visibility = View.GONE
//                binding.frNativeAds.removeAllViews()
//            }
//
//        } else {
//            binding.frNativeAds.visibility = View.GONE
//            binding.frNativeAds.removeAllViews()
//        }
    }
}