package com.musical.instrument.simulator.app.model

data class PianoRecord(
    var id: Long,
    var fileName: String,
    var jsonString: String,
    var createdAt: Long,
    var isSelected: Boolean = false
)