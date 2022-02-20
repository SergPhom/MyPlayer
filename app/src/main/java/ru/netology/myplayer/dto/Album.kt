package ru.netology.myplayer.dto

data class Album(
    val id: Int,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<Track>
)