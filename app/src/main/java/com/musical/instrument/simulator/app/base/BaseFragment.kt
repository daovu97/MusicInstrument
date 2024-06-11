package com.musical.instrument.simulator.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<B : ViewBinding>(private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    Fragment() {
    protected lateinit var binding: B

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = inflate.invoke(inflater, container, false)
        initView()
        return binding.root
    }

    abstract fun initView()
}

abstract class BaseVMFragment<B : ViewBinding, VM : BaseViewModel>(inflate: (LayoutInflater, ViewGroup?, Boolean) -> B) :
    BaseFragment<B>(inflate) {
    abstract val viewModel: VM
}