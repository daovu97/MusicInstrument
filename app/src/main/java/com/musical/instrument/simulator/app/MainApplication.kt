package com.musical.instrument.simulator.app

import android.annotation.SuppressLint
import android.content.Context
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import com.google.firebase.FirebaseApp
import com.musical.instrument.simulator.app.base.Settings
import com.musical.instrument.simulator.app.screens.splash.SplashActivity
import com.musical.instrument.simulator.app.utils.FBConfig
import com.nlbn.ads.util.AdsApplication
import com.nlbn.ads.util.AppOpenManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : AdsApplication() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        lateinit var CONTEXT: Context

    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
        Settings.migrate()
        FBConfig.shared.config()
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        FirebaseApp.initializeApp(this)
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
    }

    override fun enableAdsResume(): Boolean = FBConfig.shared.isShowAdsInter()
    override fun enableAdjustTracking(): Boolean = false

    override fun getListTestDeviceId(): MutableList<String>? = null

    override fun getResumeAdId(): String = getString(R.string.appopen_resume)
    override fun getAdjustToken(): String? = null
    override fun buildDebug(): Boolean = BuildConfig.DEBUG
}