package ru.netology.myplayer.observer

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import okhttp3.internal.wait
import ru.netology.myplayer.BuildConfig.BASE_URL
import ru.netology.myplayer.dto.Track
import ru.netology.myplayer.viewmodel.TrackViewModel


class MediaLifecycleObserver : LifecycleObserver {
    var player: MediaPlayer? = MediaPlayer()

    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepareAsync()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        player?.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        player?.release()
        player = null
    }
}