package com.musical.instrument.simulator.app.screens.language

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.adapter.LanguageAdapter
import com.musical.instrument.simulator.app.base.ActionType
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.base.Language
import com.musical.instrument.simulator.app.base.LocaleHelper
import com.musical.instrument.simulator.app.base.Settings
import com.musical.instrument.simulator.app.base.setOnBackClick
import com.musical.instrument.simulator.app.base.setOnImageRightClick
import com.musical.instrument.simulator.app.data.local.sharedKey
import com.musical.instrument.simulator.app.databinding.ActivityLanguageBinding
import com.musical.instrument.simulator.app.screens.main.HomeActivity
import com.musical.instrument.simulator.app.screens.permission.PermissionActivity
import com.musical.instrument.simulator.app.utils.AdsNative
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {

    companion object {
        val IS_FROM_SETTINGS by sharedKey()
    }

    lateinit var adapter: LanguageAdapter

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        val isFromSetting = intent?.extras?.getBoolean(IS_FROM_SETTINGS) == true
        if (!isFromSetting) {
            binding.headerLanguage.imageLeftDrawable = null
            binding.headerLanguage.actionLeftType = ActionType.NONE
        }
        binding.recLanguages.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val allValue = Language.getAll()
        adapter = LanguageAdapter(context = this, allValue) { it, i ->
            adapter.setSelected(it.id)
        }

        binding.recLanguages.adapter = adapter

        adapter.setSelected(Language.current.id)

        val selected = Language.getAll().indexOfFirst { it.id == Language.current.id }
        if (selected >= 0) {
            binding.recLanguages.scrollToPosition(selected)
        }

        binding.headerLanguage.setOnBackClick {
            finish()
        }

        binding.headerLanguage.setOnImageRightClick {
            Settings.PASS_LANGUAGE = true
            if (!isFromSetting) {
                Language.setCurrent(adapter.selectedID)
                this.let {
                    LocaleHelper.setLocale(it, Language.current.localizeCode, false)
                    val intent = Intent(it, PermissionActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Language.setCurrent(adapter.selectedID)
                LocaleHelper.setLocale(this, Language.current.localizeCode, false)
                this.let {
                    val intent = Intent(it, HomeActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }
        }
        loadNativeLanguage()
    }

    private fun loadNativeLanguage() {
        AdsNative.loadNativeLanguage(this, R.layout.layout_shimer_native_language,
            onAdLoaded = { ads, child ->
                binding.frAds.removeAllViews()
                binding.frAds.addView(child)
                lifecycleScope.launch {
                    delay(100)
                    val selected =
                        Language.getAll().indexOfFirst { it.id == Language.current.id }
                    if (selected >= 0) {
                        binding.recLanguages.scrollToPosition(selected)
                    }
                }

            }, onFail = {
                binding.frAds.removeAllViews()
            })
    }
}