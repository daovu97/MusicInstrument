package com.musical.instrument.simulator.app.screens.intro

import android.content.Context
import android.content.SharedPreferences

object Utils {

    const val KEY_IS_FIRST_INSTALL_APP = "KEY_IS_FIRST_INSTALL_APP"
    const val VALUE_IS_FIRST_INSTALL_APP = "VALUE_IS_FIRST_INSTALL_APP"
    const val VALUE_DEFAULT_IS_FIRST_INSTALL_APP = "VALUE_DEFAULT_IS_FIRST_INSTALL_APP"
    private const val NAME_THEME_SHARED = "NAME_THEME_SHARED"

    fun getIsFirstInstallApp(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME_THEME_SHARED, Context.MODE_PRIVATE)
        return sharedPreferences.getString(
            KEY_IS_FIRST_INSTALL_APP,
            VALUE_DEFAULT_IS_FIRST_INSTALL_APP
        )!!
    }

    fun saveIsFirstInstallApp(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(NAME_THEME_SHARED, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_IS_FIRST_INSTALL_APP, VALUE_IS_FIRST_INSTALL_APP)
        editor.apply()
    }

}