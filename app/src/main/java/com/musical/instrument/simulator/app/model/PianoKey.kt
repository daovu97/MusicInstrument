package com.musical.instrument.simulator.app.model

data class PianoKey(
    var type: Int,
    var group: Int,
    var position: Int,
    var currentTime: Long
) {
}