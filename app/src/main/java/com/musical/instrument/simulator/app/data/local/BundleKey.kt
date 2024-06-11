package com.musical.instrument.simulator.app.data.local

import com.musical.instrument.simulator.app.BuildConfig
import kotlin.properties.ReadOnlyProperty

fun sharedKey() =
    ReadOnlyProperty<Any, String> { thisRef, property ->
        return@ReadOnlyProperty BuildConfig.APPLICATION_ID + property.name.uppercase()
    }