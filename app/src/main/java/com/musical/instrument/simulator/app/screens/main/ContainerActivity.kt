package com.musical.instrument.simulator.app.screens.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.data.local.sharedKey
import com.musical.instrument.simulator.app.databinding.ActivityContainerFragBinding
import com.musical.instrument.simulator.app.screens.record.RecordListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerActivity :
    BaseActivity<ActivityContainerFragBinding>(ActivityContainerFragBinding::inflate) {

    companion object {
        val CHILD_FRG by sharedKey()
        val CHILD_FRG_BUNDLE by sharedKey()

        fun start(
            activity: Activity, child: ChildFrag, bundle: Bundle = bundleOf()
        ) {
            activity.startActivity(Intent(activity, ContainerActivity::class.java).apply {
                putExtra(CHILD_FRG, child.id)
                putExtra(CHILD_FRG_BUNDLE, bundle)
            })
        }
    }

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        ChildFrag.get(intent.extras?.getInt(CHILD_FRG))?.let {
            val tr = supportFragmentManager.beginTransaction()
            val fr = it.fragment
            fr.arguments = intent.extras?.getBundle(CHILD_FRG_BUNDLE)
            tr.add(R.id.container, fr)
            tr.commitAllowingStateLoss()
        } ?: finish()
    }

    enum class ChildFrag(val id: Int) {
        RECORDLIST(0);

        val fragment: Fragment
            get() = when (this) {
                RECORDLIST -> RecordListFragment()
            }

        companion object {
            fun get(id: Int?): ChildFrag? = values().firstOrNull { it.id == id }
        }
    }

}
