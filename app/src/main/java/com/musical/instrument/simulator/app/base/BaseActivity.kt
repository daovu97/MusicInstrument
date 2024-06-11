package com.musical.instrument.simulator.app.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.screens.theme.ThemeActivity
import com.musical.instrument.simulator.app.screens.theme.ThemeBackground
import kotlin.reflect.KClass


typealias MyActivity = BaseActivity<*>

abstract class BaseActivity<B : ViewBinding>(private val inflate: (LayoutInflater) -> B) :
    AppCompatActivity() {

    open val binding: B by lazy { inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideNavigationBar()
        setupView(savedInstanceState)
        setStatusBarGradiant()
    }

    override fun onResume() {
        super.onResume()

        binding.root.findViewWithTag<ImageView>("background")?.let {
            changeBackground(it)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MyContextWrapper.wrap(newBase, Language.current.localizeCode))
    }

    open fun setupView(savedInstanceState: Bundle?) {}


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideNavigationBar()
        }
    }

//    val permissionsResult =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            if (permissions.entries.all { it.value }) {
//                onPermissionGranted(permissions)
//            } else {
//                onPermissionDenied(permissions)
//            }
//        }

    open fun onPermissionGranted(permissions: Map<String, Boolean>) {}

    open fun onPermissionDenied(permissions: Map<String, Boolean>) {}

    fun changeBackground(
        background: ImageView,
        bg: ThemeBackground = ThemeBackground.current
    ) {
        bg.source?.let {
            Glide.with(this).load(it)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(background)
        }
    }

    fun setStatusBarGradiant() {
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }
}

abstract class BaseVMActivity<B : ViewBinding, VM : BaseViewModel>(private val inflate: (LayoutInflater) -> B) :
    BaseActivity<B>(inflate) {
    abstract val viewModel: VM
}

fun AppCompatActivity.hideNavigationBar() {
    window.decorView.apply {
        systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
    WindowCompat.setDecorFitsSystemWindows(window, true)
    val controllerCompat = WindowInsetsControllerCompat(window, window.decorView)
    controllerCompat.hide(WindowInsetsCompat.Type.navigationBars())
    controllerCompat.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    controllerCompat.isAppearanceLightStatusBars = true
}

