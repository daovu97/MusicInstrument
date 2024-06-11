package com.musical.instrument.simulator.app.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.musical.instrument.simulator.app.HomeFragment
import com.musical.instrument.simulator.app.base.BaseVMActivity
import com.musical.instrument.simulator.app.base.BaseViewModel
import com.musical.instrument.simulator.app.databinding.ActivityMainBinding
import com.musical.instrument.simulator.app.screens.record.RecordListFragment
import com.nlbn.ads.util.AppOpenManager
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity :
    BaseVMActivity<ActivityMainBinding, HomeViewModel>(ActivityMainBinding::inflate) {

    override val viewModel: HomeViewModel by (this as ComponentActivity).viewModels()

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        initViewPager()
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().enableAppResumeWithActivity(HomeActivity::class.java)
    }

    private fun initViewPager() {
        binding.viewPager.offscreenPageLimit = 3
        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> HomeFragment()
                    1 -> RecordListFragment()
                    else -> SettingFragment()
                }
            }

            override fun getItemCount(): Int {
                return 3
            }
        }
        binding.tabBarView.setOnTabChangeListener {
            viewModel.currentSelected.postValue(it)
        }
        viewModel.currentSelected.observe(this) {
            binding.tabBarView.currentSelectedIndex = it
            binding.viewPager.setCurrentItem(it, false)
        }
    }
}

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    val currentSelected = MutableLiveData(0)
}

fun ViewPager2.findCurrentFragment(fragmentManager: FragmentManager): Fragment? {
    return fragmentManager.findFragmentByTag("f$currentItem")
}