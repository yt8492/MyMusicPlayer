package jp.zliandroid.mymusicplayer.tracklist

import jp.zliandroid.mymusicplayer.BasePresenter
import jp.zliandroid.mymusicplayer.BaseView
import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.data.Track

interface TrackListContract {
    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun showAlbumInfo(album: Album)
        fun showTracks(tracks: List<Track>)
        fun showMusicPlayUi(tracks: List<Track>, position: Int)
    }

    interface Presenter : BasePresenter {
        fun showAlbumInfo()
        fun listTracks()
        fun openTrack(tracks: List<Track>, position: Int)
    }
}