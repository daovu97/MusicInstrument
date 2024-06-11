package com.musical.instrument.simulator.app.screens.piano

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.SongAdapter
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.databinding.ActivityMusicListBinding
import com.musical.instrument.simulator.app.model.Song
import com.nlbn.ads.callback.NativeCallback
import com.nlbn.ads.util.Admob

class MusicListActivity :
    BaseActivity<ActivityMusicListBinding>(ActivityMusicListBinding::inflate) {

    private lateinit var songAdapter: SongAdapter
    private lateinit var list: MutableList<Song>

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
        loadAdsNativeMusicList()
    }

    private fun init() {
        list = mutableListOf()
        list = Utils.getSongs()
        songAdapter = SongAdapter(this@MusicListActivity, list, object : SongAdapter.ClickItem {
            override fun clickItem(item: Song, index: Int) {
                val index = item.id
                val intent = Intent()
                intent.putExtra("index_item", index)
                setResult(RESULT_OK, intent)
                finish()
            }
        })
        binding.rcv.apply {
            adapter = songAdapter
        }
    }

    private fun onClick() {
        binding.iconBack.setOnClickListener { finish() }
        binding.iconDone.setOnClickListener {
            binding.iconDone.isActivated = !binding.iconDone.isActivated
            if (!binding.iconDone.isActivated) {
                list.sortByDescending { it.name }
            } else {
                list.sortBy { it.name }
            }
            songAdapter.setListSong(list)
        }
    }

    private fun loadAdsNativeMusicList() {
        Admob.getInstance().loadNativeAd(
            this@MusicListActivity,
            getString(R.string.native_music),
            object : NativeCallback() {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    val adView = LayoutInflater.from(this@MusicListActivity)
                        .inflate(R.layout.ads_native_small, null)
                    val nativeAdView = adView as NativeAdView
                    binding.frNativeAds.removeAllViews()
                    binding.frNativeAds.addView(adView)
                    Admob.getInstance().pushAdsToViewCustom(nativeAd, nativeAdView)
                }

                override fun onAdFailedToLoad() {
                    binding.frNativeAds.removeAllViews()
                }
            }
        )
    }

}