package com.musical.instrument.simulator.app.model

data class Song(
    val id: Int,
    val name: String,
    val singleName: String
) : Comparable<Song> {
    override fun compareTo(other: Song): Int {
        return name.compareTo(other.name)
    }

    override fun toString(): String {
        return name
    }
}