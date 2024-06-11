package com.musical.instrument.simulator.app.screens.main

import android.content.Intent
import android.os.Bundle
import com.musical.instrument.simulator.app.BuildConfig
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseFragment
import com.musical.instrument.simulator.app.base.gone
import com.musical.instrument.simulator.app.base.launchMarket
import com.musical.instrument.simulator.app.base.launchUrl
import com.musical.instrument.simulator.app.base.sendFeedback
import com.musical.instrument.simulator.app.base.setOnBackClick
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.base.shareThisApp
import com.musical.instrument.simulator.app.databinding.FragmentSettingBinding
import com.musical.instrument.simulator.app.screens.language.LanguageActivity
import com.musical.instrument.simulator.app.screens.language.LanguageActivity.Companion.IS_FROM_SETTINGS
import com.musical.instrument.simulator.app.screens.theme.ThemeActivity
import com.musical.instrument.simulator.app.utils.getAdmobIfNeed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    override fun initView() {

        binding.btnTheme.setOnSingleClickListener {
            val intent = Intent(activity, ThemeActivity::class.java)
            startActivity(intent)
        }
        binding.btnLanguage.setOnSingleClickListener {
            val bundle = Bundle()
            bundle.putBoolean(IS_FROM_SETTINGS, true)
            val intent = Intent(activity, LanguageActivity::class.java)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        binding.btnRateUs.setOnSingleClickListener {
            activity?.launchMarket()
        }

        binding.btnShare.setOnSingleClickListener {
            activity?.shareThisApp()
        }

        binding.btnPrivacyPolicy.setOnSingleClickListener {
            activity?.launchUrl(BuildConfig.URL_PRIVACY)
        }

        binding.btnFeedback.setOnClickListener {
            activity?.sendFeedback()
        }

        binding.headerSetting.setOnBackClick {

        }

        loadInterSetting()
    }

    private fun loadInterSetting() {
        try {
            activity?.getAdmobIfNeed()?.loadBanner(activity, getString(R.string.banner))
                ?: binding.rlBannerList.gone()
        } catch (e: java.lang.Exception) {
            binding.rlBannerList.gone()
        }
    }
}