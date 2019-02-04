package jp.zliandroid.mymusicplayer.tracklist

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
        val album = albumRepository.getAlbum(albumId)
        trackListView.showAlbumInfo(album)
    }

    override fun listTracks() {
        if (trackListView.isActive) {
            val tracks = trackRepository.getTracks(albumId)
            trackListView.showTracks(tracks)
        }
    }

    override fun openTrack(albumId: Long, tracks: List<Track>, position: Int) {
        trackListView.showMusicPlayUi(albumId, tracks, position)
    }
}