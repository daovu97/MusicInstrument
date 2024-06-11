package com.musical.instrument.simulator.app.screens.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.LoadAdError
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.base.Settings
import com.musical.instrument.simulator.app.databinding.ActivitySplashBinding
import com.musical.instrument.simulator.app.screens.language.LanguageActivity
import com.musical.instrument.simulator.app.screens.main.HomeActivity
import com.musical.instrument.simulator.app.screens.permission.PermissionActivity
import com.musical.instrument.simulator.app.utils.getAdmobInterIfNeed
import com.nlbn.ads.callback.InterCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate) {

    private var interCallback: InterCallback? = null
    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        loadInterAdsSplash()
    }

    private fun loadInterAdsSplash() {
        fun delayShow() {
            lifecycleScope.launch {
                delay(1500)
                handleNavigation()
            }
        }
        interCallback = object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                handleNavigation()
            }

            override fun onAdFailedToLoad(i: LoadAdError?) {
                super.onAdFailedToLoad(i)
                handleNavigation()
            }
        }

        getAdmobInterIfNeed()?.loadSplashInterAds2(
            this,
            getString(R.string.inter_splash),
            3000,
            interCallback
        ) ?: delayShow()

    }

    override fun onResume() {
        super.onResume()
        getAdmobInterIfNeed()?.onCheckShowSplashWhenFail(this, interCallback, 1000)
    }

    override fun onStop() {
        super.onStop()
        getAdmobInterIfNeed()?.dismissLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        getAdmobInterIfNeed()?.dismissLoadingDialog()
    }

    private fun handleNavigation() {
        if (!Settings.PASS_LANGUAGE) {
            val intent = Intent(this@SplashActivity, LanguageActivity::class.java)
            startActivity(intent)
        } else if (!Settings.PASS_TUTORIAL) {
            startActivity(Intent(this@SplashActivity, PermissionActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
        }
        finish()
    }
}