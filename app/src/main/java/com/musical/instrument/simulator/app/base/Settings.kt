package com.musical.instrument.simulator.app.base

import android.content.Context
import android.content.SharedPreferences
import com.musical.instrument.simulator.app.BuildConfig
import com.musical.instrument.simulator.app.MainApplication
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

enum class PrefMigrate(val newVersion: Int) {
    M_0_1(1);

    fun migrate() {
        when (this) {
            M_0_1 -> {
                if (Settings.VERSION_P == 0) {
                    Settings.VERSION_P = newVersion
                }
            }
        }
    }
}

object Settings {
    const val VERSION = 1
    private val prefs =
        MainApplication.CONTEXT.getSharedPreferences(
            BuildConfig.APPLICATION_ID,
            Context.MODE_PRIVATE
        )

    var PASS_TUTORIAL by sharedPref(prefs, false)
    var PASS_LANGUAGE by sharedPref(prefs, false)
    var VERSION_P by sharedPref(prefs, 0)
    var CURRENT_THEME_BG by sharedPref(prefs, -1)
    var APP_LANGUAGE by sharedPref(prefs, -1)

    fun migrate() {
        PrefMigrate.values().filter { it.newVersion <= VERSION }.sortedBy { it.newVersion }
            .forEach { it.migrate() }
    }
}

inline fun <reified T> sharedPref(prefs: SharedPreferences, defaultValue: T = defaultForType()) =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>) =
            prefs[getKey(thisRef, property), defaultValue]

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            prefs[getKey(thisRef, property)] = value
        }

        private fun getKey(thisRef: Any, property: KProperty<*>) = property.name
    }

inline fun <reified T> defaultForType(): T =
    when (T::class) {
        String::class -> "" as T
        Int::class -> 0 as T
        Boolean::class -> false as T
        Float::class -> 0F as T
        Long::class -> 0L as T
        else -> throw IllegalArgumentException("Default value not found for type ${T::class}")
    }

inline operator fun <reified T> SharedPreferences.get(key: String, defaultValue: T): T {
    when (T::class) {
        Boolean::class -> return this.getBoolean(key, defaultValue as Boolean) as T
        Float::class -> return this.getFloat(key, defaultValue as Float) as T
        Double::class -> return java.lang.Double.longBitsToDouble(
            this.getLong(
                key,
                java.lang.Double.doubleToLongBits(defaultValue as Double)
            )
        ) as T

        Int::class -> return this.getInt(key, defaultValue as Int) as T
        Long::class -> return this.getLong(key, defaultValue as Long) as T
        String::class -> return this.getString(key, defaultValue as String) as T
        else -> {
            if (defaultValue is Set<*>) {
                return this.getStringSet(key, defaultValue as Set<String>) as T
            }
        }
    }

    return defaultValue
}

inline operator fun <reified T> SharedPreferences.set(key: String, value: T) {
    val editor = this.edit()
    when (T::class) {
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Float::class -> editor.putFloat(key, value as Float)
        Double::class -> editor.putLong(key, java.lang.Double.doubleToLongBits(value as Double))
        Int::class -> editor.putInt(key, value as Int)
        Long::class -> editor.putLong(key, value as Long)
        String::class -> editor.putString(key, value as String)
        else -> {
            if (value is Set<*>) {
                editor.putStringSet(key, value as Set<String>)
            }
        }
    }

    editor.commit()
}