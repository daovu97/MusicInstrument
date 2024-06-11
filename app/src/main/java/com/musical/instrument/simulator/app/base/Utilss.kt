package com.musical.instrument.simulator.app.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.musical.instrument.simulator.app.BuildConfig
import com.musical.instrument.simulator.app.R
import com.nlbn.ads.util.AppOpenManager

fun Context.compatColor(int: Int): Int {
    return ContextCompat.getColor(this, int)
}

fun Context.compactDrawable(int: Int): Drawable? {
    return ContextCompat.getDrawable(this, int)
}

fun Context.launchMarket() {
    val uri = Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
    val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
    try {
        startActivity(myAppLinkToMarket)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "Unable to find market app", Toast.LENGTH_LONG).show()
    }
}

fun Context.shareThisApp() {
    AppOpenManager.getInstance().disableAppResume()
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
    shareIntent.putExtra(
        Intent.EXTRA_TEXT,
        "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
    )
    startActivity(Intent.createChooser(shareIntent, "Share"))
}

fun Context.sendFeedback(
    toEmail: String = BuildConfig.EMAIL_ADDRESS,
    subject: String = getString(R.string.app_name) + "Support",
    text: String = "\n\n\n Version:" + BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")"
) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(toEmail))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
    }
    try {
        startActivity(intent)
    } catch (e: Exception) {
        //error message
    }
}

fun Context.launchUrl(url: String) {
    AppOpenManager.getInstance().disableAppResume()
    val intent = Intent(
        Intent.ACTION_VIEW, Uri.parse(url)
    )
    try {
        startActivity(intent)
    } catch (e: Exception) {
        //error message
    }
}

fun Context.launchSetting(){
    val intent = Intent().apply {
        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        data = Uri.fromParts("package", packageName, null)
    }
    startActivity(intent)
}

fun Context.hasPermissions(permissions: Array<String>): Boolean = permissions.all {
    ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun MyActivity.checkPermission(
    perms: Array<String>
) {
    if (hasPermissions(perms)) {
        val a = mutableMapOf<String, Boolean>()
        perms.forEach { s -> a[s] = true }
        onPermissionGranted(a)
    } else {
//        permissionsResult.launch(perms)
    }
}

object Converter {
    fun dpFromPx(px: Int): Float {
        return px / Resources.getSystem().displayMetrics.density
    }

    fun pixelsToSp(pixels: Int): Float {
        val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
        return pixels / scaledDensity
    }

    fun asPixels(value: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        val dpAsPixels = (value * scale + 0.5f)
        return dpAsPixels.toInt()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
    this.isEnabled = true
}

fun View.hidden() {
    this.visibility = View.INVISIBLE
    this.isEnabled = false
}

fun View.gone() {
    this.visibility = View.GONE
    this.isEnabled = false
}