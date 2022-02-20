package ru.netology.myplayer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.netology.myplayer.dto.Album
import ru.netology.myplayer.dto.Track
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class TrackViewModel: ViewModel() {

    var data = MutableLiveData<List<Track>>()

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<Album>() {}
    lateinit var album: Album

    fun playing(track: Track){
        data.value = data.value?.map { if(it.id == track.id){
            it.copy(isPlaying = true)
        } else it}
    }

    fun stopped(track: Track){
        data.value = data.value?.map { if(it.id == track?.id){
            it.copy(isPlaying = false)
        } else it}
    }
    fun getAll() = thread {
            val request: Request = Request.Builder()
                .url("https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json")
                .build()
            try {
                client.newCall(request)
                    .execute()
                    .let { it.body?.string() ?: throw RuntimeException("body is null") }
                    .let {
                        album = gson.fromJson(it, typeToken.type)
                        data.postValue(album.tracks)
                    }
            }catch (e: Throwable){ println(" error is $e")}
    }



}