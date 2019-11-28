package jp.zliandroid.mymusicplayer.musicplay

import android.content.Context
import android.os.Bundle
import android.os.SystemClock

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.android.support.AndroidSupportInjection
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.data.Album

import jp.zliandroid.mymusicplayer.data.Track
import kotlinx.android.synthetic.main.fragment_music_play.*
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 *
 */
class MusicPlayFragment : Fragment(), MusicPlayContract.View {
    override val isActive: Boolean
        get() = isAdded

    @Inject
    override lateinit var presenter: MusicPlayContract.Presenter

    private val exoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(requireContext()).apply {
            addListener(object : Player.EventListener {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        presenter.playNext()
                    }
                }
            })
        }
    }

    private val dataSourceFactory by lazy {
        DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), "MyMusicPlayer")
        )
    }

    private val mediaSourceFactory by lazy {
        ProgressiveMediaSource.Factory(dataSourceFactory)
    }

    private var playing = false

    private var job: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_play, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
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
        exoPlayer.apply {
            val mediaSource = mediaSourceFactory.createMediaSource(track.uri)
            prepare(mediaSource)
            playWhenReady = true
        }

        playing = true
        job = lifecycleScope.launch {
            while (true) {
                withContext(Dispatchers.Default) {
                    delay(100)
                }
                if (playing) {
                    withContext(Dispatchers.Main.immediate) {
                        seek_bar.progress = exoPlayer.currentPosition.toInt()
                        meter_now.base = SystemClock.elapsedRealtime() - seek_bar.progress
                    }
                }
            }
        }
    }

    override fun playStop() {
        exoPlayer.playWhenReady = false
        playing = false
        job = null
    }

    override fun playPause() {
        exoPlayer.playWhenReady = false
        playing = false
        music_play.setImageResource(R.drawable.start)
    }

    override fun playResume() {
        playing = true
        music_play.setImageResource(R.drawable.stop)
        exoPlayer.playWhenReady = true
    }

    override fun seekTo(milliSec: Long) {
        exoPlayer.seekTo(milliSec)
    }

    override fun finish() {
        exoPlayer.release()
        activity?.finish()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MusicPlayFragment()
    }
}
