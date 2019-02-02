package jp.zliandroid.mymusicplayer.tracklist

import android.util.Log
import jp.zliandroid.mymusicplayer.data.Track
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import jp.zliandroid.mymusicplayer.data.tracksource.TrackRepository

class TrackListPresenter(private val albumId: Long, private val albumRepository: AlbumRepository, private val trackRepository: TrackRepository, private val trackListView: TrackListContract.View)
    : TrackListContract.Presenter {
    init {
        trackListView.presenter = this
    }

    override fun start() {
        showAlbumInfo()
        listTracks()
    }

    override fun showAlbumInfo() {
        Log.d("debug", albumId.toString())
        val album = albumRepository.getAlbum(albumId)
        trackListView.showAlbumInfo(album)
    }

    override fun listTracks() {
        if (trackListView.isActive) {
            val tracks = trackRepository.getTracks(albumId)
            trackListView.showTracks(tracks)
        }
    }

    override fun openTrack(tracks: List<Track>, position: Int) {
        trackListView.showMusicPlayUi(tracks, position)
    }
}