package com.musical.instrument.simulator.app.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.musical.instrument.simulator.app.MainApplication
import com.musical.instrument.simulator.app.R
import com.nlbn.ads.callback.InterCallback
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob

fun Context.getAdmobIfNeed(): Admob? {
    return if (checkNetWork(this) == true) {
        return if (FBConfig.shared.isShowAds()) Admob.getInstance() else null
    } else null
}

fun Context.getAdmobInterIfNeed(): Admob? {
    return if (checkNetWork(this) == true) {
        return if (FBConfig.shared.isShowAdsInter()) getAdmobIfNeed() else null
    } else null
}

fun checkNetWork(context: Context?): Boolean? {
    if (context == null) {
        return false
    }
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capability =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capability != null) {
            if (capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capability.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            return true
        }
    }
    return false
}


enum class AdsInter {

    THEME, PIANO, STYLE, ALL;

    var interAd: InterstitialAd?
        get() = when (this) {
            THEME -> inter_theme
            PIANO -> inter_piano
            STYLE -> inter_style
            ALL -> inter_all
        }
        set(value) {
            when (this) {
                THEME -> inter_theme = value
                PIANO -> inter_piano = value
                STYLE -> inter_style = value
                ALL -> inter_all = value
            }
        }

    @get:StringRes
    val unitID: Int
        get() = when (this) {
            THEME -> R.string.inter_theme
            PIANO -> R.string.inter_piano
            STYLE -> R.string.inter_style
            ALL -> R.string.inter_all
        }

    fun load(activity: Context = MainApplication.CONTEXT) {
        if (interAd != null) return
        activity.getAdmobInterIfNeed()
            ?.loadInterAds(activity, activity.getString(unitID), object : InterCallback() {
                override fun onInterstitialLoad(interstitialAd: InterstitialAd) {
                    super.onInterstitialLoad(interstitialAd)
                    interAd = interstitialAd
                }
            })
    }

    fun showInterAds(
        activity: Context, onNextAction: (() -> Unit)? = null, onFail: ((AdError?) -> Unit)? = null
    ) {
        activity.getAdmobInterIfNeed()?.showInterAds(activity, interAd, object : InterCallback() {
            override fun onNextAction() {
                super.onNextAction()
                interAd = null
                load(MainApplication.CONTEXT)
                onNextAction?.invoke()
            }

            override fun onAdFailedToShow(adError: AdError?) {
                super.onAdFailedToShow(adError)
                onFail?.invoke(adError)
                interAd = null
                load(MainApplication.CONTEXT)
            }
        }) ?: onFail?.invoke(null)
    }

    companion object {
        var inter_theme: InterstitialAd? = null
        var inter_piano: InterstitialAd? = null
        var inter_style: InterstitialAd? = null
        var inter_all: InterstitialAd? = null
    }

}

enum class AdsNative {
    THEME, LANGUAGE, LANGUAGE_1, LANGUAGE_2, HOME, RECORD, MUSIC;

    @get:StringRes
    val unitID: Int
        get() = when (this) {
            THEME -> R.string.native_theme
            LANGUAGE -> R.string.native_language
            LANGUAGE_1 -> R.string.native_language_2
            LANGUAGE_2 -> R.string.native_language_3
            HOME -> R.string.native_home
            RECORD -> R.string.native_record
            MUSIC -> R.string.native_music
        }

    fun loadAds(
        activity: Context?,
        @LayoutRes intoView: Int,
        onAdLoaded: ((NativeAd, View) -> Unit)? = null,
        onFail: ((AdError?) -> Unit)? = null
    ) {
        activity?.getAdmobIfNeed()
            ?.loadNativeAd(activity, activity.getString(unitID), object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    val adView = LayoutInflater.from(activity).inflate(intoView, null)
                    val nativeAdView = adView as? NativeAdView ?: return
                    onAdLoaded?.invoke(nativeAd, adView)
                    Admob.getInstance().pushAdsToViewCustom(nativeAd, nativeAdView)
                }

                override fun onAdFailedToLoad() {
                    onFail?.invoke(null)
                }
            }) ?: onFail?.invoke(null)
    }

    fun loadAds(
        activity: Context?,
        @LayoutRes intoView: Int, container: ViewGroup
    ) {
        if (activity?.getAdmobIfNeed() == null) {
            container.removeAllViews()
        }
        loadAds(activity, intoView, onAdLoaded = { ads, child ->
            container.removeAllViews()
            container.addView(child)
        }) {
            container.removeAllViews()
        }
    }

    companion object {
        fun loadNativeLanguage(
            activity: Context,
            @LayoutRes intoView: Int,
            onAdLoaded: ((NativeAd, View) -> Unit)? = null,
            onFail: ((AdError?) -> Unit)? = null
        ) {
            LANGUAGE.loadAds(activity, intoView, onAdLoaded = onAdLoaded) {
                LANGUAGE_1.loadAds(activity, intoView, onAdLoaded) {
                    LANGUAGE_2.loadAds(activity, intoView, onAdLoaded, onFail)
                }
            }
        }
    }
}