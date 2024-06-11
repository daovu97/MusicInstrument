package com.musical.instrument.simulator.app.screens.permission

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.musical.instrument.simulator.app.R
import com.musical.instrument.simulator.app.base.BaseActivity
import com.musical.instrument.simulator.app.base.setOnSingleClickListener
import com.musical.instrument.simulator.app.databinding.ActivityPermissionBinding
import com.musical.instrument.simulator.app.screens.main.HomeActivity
import com.musical.instrument.simulator.app.utils.AdsNative
import com.nlbn.ads.util.AppOpenManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionActivity :
    BaseActivity<ActivityPermissionBinding>(ActivityPermissionBinding::inflate) {

    private var checkPermissionStorage: Boolean? = null
    private var checkPermissionRecord: Boolean? = null

    private var checkOpen = false

    override fun setupView(savedInstanceState: Bundle?) {
        super.setupView(savedInstanceState)
        init()
        onClick()
    }

    private fun init() {
        checkPermissionRecord =
            Utils.checkPermissionRecordAudio(this@PermissionActivity, binding.switchRecord)
        checkPermissionStorage =
            Utils.checkPermissionStorage(this@PermissionActivity, binding.switchStorage)

        loadAdsNativePermission()
    }

    private fun onClick() {
        binding.switchRecord.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked && view.isPressed) {
                Utils.requestPermissionRecordAudio(this@PermissionActivity)
            }
        }

        binding.switchStorage.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked && view.isPressed) {
                Utils.requestPermissionStorage(
                    this@PermissionActivity,
                    this@PermissionActivity,
                    storageActivityResultLauncher
                )
            }
        }

        binding.header.actionRightView?.setOnSingleClickListener {
            go()
        }
    }

    private fun go() {
        if (checkPermissionRecord!! && checkPermissionStorage!!) {
            com.musical.instrument.simulator.app.base.Settings.PASS_TUTORIAL = true
            startActivity(Intent(this@PermissionActivity, HomeActivity::class.java))
            finish()
        } else {
            Toast.makeText(
                this@PermissionActivity,
                getString(R.string.noti_need_permission),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private val storageActivityResultLauncher =
        (this as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setCancelable(false)

            binding.frNativeAds.visibility = View.GONE

            alertDialog.setMessage(getString(R.string.noti_need_permission))
            alertDialog.setButton(
                -1, getString(R.string.go_to_setting)
            ) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", (this as ComponentActivity).packageName, null)
                intent.data = uri
                AppOpenManager.getInstance()
                    .disableAppResumeWithActivity(PermissionActivity::class.java)
                checkOpen = true
                startActivityForResult(intent, Utils.STORAGE_PERMISSION_CODE)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Utils.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (write && read) {
                    checkPermissionStorage = true
                    binding.switchStorage.isChecked = true
                    binding.switchStorage.isEnabled = false

                    binding.frNativeAds.visibility = View.VISIBLE

                } else {
                    checkPermissionStorage = false
                    binding.switchStorage.isChecked = false
                    binding.switchStorage.isEnabled = true

                    binding.frNativeAds.visibility = View.GONE

                    val alertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setCancelable(false)

                    alertDialog.setMessage(getString(R.string.noti_need_permission))
                    alertDialog.setButton(
                        -1,
                        getString(R.string.go_to_setting)
                    ) { dialogInterface, i ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri

                        AppOpenManager.getInstance()
                            .disableAppResumeWithActivity(PermissionActivity::class.java)

                        startActivityForResult(intent, 1234)
                        checkOpen = true

                        alertDialog.dismiss()
                    }



                    alertDialog.show()

                }
            }
        }

        if (requestCode == Utils.RECORD_AUDIO_PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val record = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if (record) {
                    checkPermissionRecord = true
                    binding.switchRecord.isChecked = true
                    binding.switchRecord.isEnabled = false

                    binding.frNativeAds.visibility = View.VISIBLE

                } else {
                    checkPermissionRecord = false
                    binding.switchRecord.isChecked = false
                    binding.switchRecord.isEnabled = true

                    binding.frNativeAds.visibility = View.GONE

                    val alertDialog = AlertDialog.Builder(this).create()
                    alertDialog.setCancelable(false)

                    alertDialog.setMessage("You need enable permission")
                    alertDialog.setButton(
                        -1,
                        "go to setting"
                    ) { dialogInterface, i ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri

                        AppOpenManager.getInstance()
                            .disableAppResumeWithActivity(PermissionActivity::class.java)

                        startActivityForResult(intent, 1234)
                        checkOpen = true
                        alertDialog.dismiss()
                    }



                    alertDialog.show()

                }
            }
        }
    }

    private fun loadAdsNativePermission() {
        AdsNative.MUSIC.loadAds(this, R.layout.ads_native_lang, binding.frNativeAds)
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().enableAppResumeWithActivity(PermissionActivity::class.java)

        if (checkOpen) {
            binding.frNativeAds.visibility = View.VISIBLE
            checkOpen = false
        }

        checkPermissionRecord =
            Utils.checkPermissionRecordAudio(this@PermissionActivity, binding.switchRecord)
        checkPermissionStorage =
            Utils.checkPermissionStorage(this@PermissionActivity, binding.switchStorage)
    }

}