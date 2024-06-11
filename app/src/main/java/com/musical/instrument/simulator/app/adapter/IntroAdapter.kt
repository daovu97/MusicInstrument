package com.musical.instrument.simulator.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.musical.instrument.simulator.app.screens.intro.fragment.FirstIntroFragment
import com.musical.instrument.simulator.app.screens.intro.fragment.SecondIntroFragment
import com.musical.instrument.simulator.app.screens.intro.fragment.ThirdIntroFragment

class IntroAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> {
                FirstIntroFragment()
            }

            1 -> {
                SecondIntroFragment()
            }

            2 -> {
                ThirdIntroFragment()
            }

            else -> {
                FirstIntroFragment()
            }
        }
}