package jp.zliandroid.mymusicplayer.musicplay

import jp.zliandroid.mymusicplayer.data.Track
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import jp.zliandroid.mymusicplayer.data.tracksource.TrackRepository

class MusicPlayPresenter(albumId: Long, trackIds: List<Long>, private var position: Int, private val albumRepository: AlbumRepository,
                         trackRepository: TrackRepository, private val musicPlayView: MusicPlayContract.View)
    : MusicPlayContract.Presenter {
    init {
        musicPlayView.presenter = this
    }

    private val album = albumRepository.getAlbum(albumId)
    private val tracks = trackRepository.getTracks(trackIds)

    override fun start() {
        playStart(tracks[position])
    }

    override fun showTrackInfo(track: Track) {
        if (musicPlayView.isActive) {
            musicPlayView.setTrackInfo(track, album)
        }
    }

    override fun playStart(track: Track) {
        if (musicPlayView.isActive) {
            showTrackInfo(track)
            musicPlayView.playStart(track)
        }
    }

    override fun playStop() {
        if (musicPlayView.isActive) {
            musicPlayView.playStop()
        }
    }

    override fun playPause() {
        if (musicPlayView.isActive) {
            musicPlayView.playPause()
        }
    }

    override fun playResume() {
        if (musicPlayView.isActive) {
            musicPlayView.playResume()
        }
    }

    override fun seekTo(milliSec: Long) {
        if (musicPlayView.isActive) {
            musicPlayView.seekTo(milliSec)
        }
    }

    override fun playNext() {
        if (position + 1 < tracks.size) {
            playStop()
            playStart(tracks[++position])
        } else {
            playStop()
            musicPlayView.finish()
        }
    }

    override fun playPrev() {
        if (position - 1 >= 0) {
            playStop()
            playStart(tracks[--position])
        } else {
            musicPlayView.seekTo(0)
        }
    }
}