package ru.netology.myplayer.dto

data class Track(
    val id: Int,
    val file: String,
    var isPlaying: Boolean = false
) {
}