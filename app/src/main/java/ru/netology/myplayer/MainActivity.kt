package ru.netology.myplayer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import ru.netology.myplayer.databinding.ActivityMainBinding
import ru.netology.myplayer.dto.Track
import ru.netology.myplayer.observer.MediaLifecycleObserver
import ru.netology.myplayer.viewmodel.TrackViewModel
import ru.netology.nmedia.adapter.Callback
import ru.netology.nmedia.adapter.TracksAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)

        val viewModel: TrackViewModel by viewModels()
        viewModel.getAll()

        val mediaObserver = MediaLifecycleObserver()
        lifecycle.addObserver(mediaObserver)
        val baseUrl = BuildConfig.BASE_URL
        val adapter = TracksAdapter(object : Callback {
            var nextTrack:Track? = null
            override fun onPlayClick(tracks:List<Track>, track: Track) {
                mediaObserver.apply {
                    player?.let { if(it.isPlaying) {
                        it.release()
                        player = null }
                    }
                    if(player == null) player = MediaPlayer()
                    player?.setDataSource("$baseUrl${track.file}")
                    player?.setOnCompletionListener {
                        nextTrack = if (nextTrack != null){
                            viewModel.stopped(nextTrack!!)
                            tracks[nextTrack!!.id]
                        } else{
                            viewModel.stopped(track)
                            tracks[track.id]
                        }
                        viewModel.playing(nextTrack!!)
                        playNextTrack(tracks, nextTrack!!)
                    }
                }.play()
                viewModel.playing(track)
            }
            fun playNextTrack(tracks: List<Track>, track: Track){
                mediaObserver.player?.let {
                    it.release()
                    mediaObserver.player = null
                    onPlayClick(tracks, track)
                }
            }

            override fun onPauseClick(tracks:List<Track>, track: Track) {
                mediaObserver.apply {
                    player?.release()
                    player = null
                    viewModel.stopped(track)
                }
            }

            override fun onSeekClick() {
                mediaObserver.apply {
                    player?.seekTo(360000)
                }
            }
        })

        binding.listItem.adapter = adapter
        viewModel.data.observe(this) { tracks ->
            adapter.trackList = tracks
        }

    }
}