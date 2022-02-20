package ru.netology.nmedia.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.myplayer.R
import ru.netology.myplayer.databinding.CardTrackBinding
import ru.netology.myplayer.dto.Track


interface Callback {
    fun onPlayClick(tracks: List<Track>, track: Track){}
    fun onPauseClick(tracks: List<Track>, track: Track){}
    fun onSeekClick(){}
}

class TracksAdapter(
    private val callback: Callback
) : androidx.recyclerview.widget.ListAdapter<Track,TrackViewHolder>(diffCallback) {
    var trackList = emptyList<Track>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
                if (oldItem::class != newItem::class){
                    return false
                }
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = CardTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, callback, trackList)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int = trackList.size
}

class TrackViewHolder(
    private val binding: CardTrackBinding,
    private val callback: Callback,
    private val trackList: List<Track>
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(track: Track) {
        binding.apply {
            binding.trackNumber.text = track.id.toString()
            playPause.isChecked = track.isPlaying
            playSeek.setIconResource(R.drawable.ic_play_seek_24)
            playSeek.visibility = if (track.isPlaying){
                View.VISIBLE
            }else View.GONE
            playSeek.setOnClickListener {
                callback.onSeekClick()
            }
            playPause.setIconResource(
                if(track.isPlaying) R.drawable.ic_pause_24
                else R.drawable.ic_play_pause_24dp)
            playPause.setOnClickListener {
                if(track.isPlaying){
                    callback.onPauseClick(tracks = trackList, track = track)
                }else  callback.onPlayClick(tracks = trackList, track = track)
            }
        }
    }
}

