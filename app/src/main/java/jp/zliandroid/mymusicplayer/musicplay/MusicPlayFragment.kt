package jp.zliandroid.mymusicplayer.musicplay

import android.media.MediaPlayer
import android.os.Bundle
import android.os.SystemClock

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.data.Album

import jp.zliandroid.mymusicplayer.data.Track
import kotlinx.android.synthetic.main.fragment_music_play.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MusicPlayFragment : Fragment(), MusicPlayContract.View {
    override val isActive: Boolean
        get() = isAdded

    override lateinit var presenter: MusicPlayContract.Presenter

    private var mediaPlayer: MediaPlayer? = null

    private var playing = false

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_play, container, false)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onPause() {
        super.onPause()
        presenter.playStop()
    }

    override fun setTrackInfo(track: Track, album: Album) {
        music_title.text = track.title
        music_album_title.text = album.title
        music_artist.text = track.artistName
        if (album.albumArtUri != null) {
            music_album_art.setImageURI(album.albumArtUri)
        } else {
            music_album_art.setImageResource(R.drawable.dummy_album_art_slim)
        }
        music_title.isSelected = true
        meter_total.base = SystemClock.elapsedRealtime() - track.duration
        seek_bar.progress = 0
        seek_bar.max = track.duration.toInt()
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                meter_now.base = SystemClock.elapsedRealtime() - progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let {
                    presenter.seekTo(it.progress.toLong())
                }
            }

        })
        music_play.setOnClickListener {
            if (playing) {
                presenter.playPause()
            } else {
                presenter.playResume()
            }
        }
        music_next.setOnClickListener {
            presenter.playNext()
        }
        music_previous.setOnClickListener {
            presenter.playPrev()
        }
    }

    override fun playStart(track: Track) {
        mediaPlayer = MediaPlayer.create(context, track.uri).apply {
            start()
            setOnCompletionListener {
                presenter.playNext()
            }
        }
        playing = true
        job = GlobalScope.launch {
            while (true) {
                delay(100)
                if (playing) {
                    withContext(Dispatchers.Main) {
                        seek_bar.progress += 100
                        meter_now.base = SystemClock.elapsedRealtime() - seek_bar.progress
                    }
                }
            }
        }
    }

    override fun playStop() {
        job?.cancel()
        job = null
        playing = false
        mediaPlayer?.apply {
            reset()
            release()
        }
        mediaPlayer = null
    }

    override fun playPause() {
        playing = false
        music_play.setImageResource(R.drawable.start)
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    override fun playResume() {
        playing = true
        music_play.setImageResource(R.drawable.stop)
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    override fun seekTo(milliSec: Long) {
        mediaPlayer?.seekTo(milliSec.toInt())
    }

    override fun finish() {
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MusicPlayFragment()
    }
}
