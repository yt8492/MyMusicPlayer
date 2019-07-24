package jp.zliandroid.mymusicplayer.albumlist

import jp.zliandroid.mymusicplayer.BasePresenter
import jp.zliandroid.mymusicplayer.BaseView
import jp.zliandroid.mymusicplayer.data.Album

interface AlbumListContract {
    interface View : BaseView<Presenter> {
        var isActive: Boolean

        fun showAlbums(albums: List<Album>)
        fun showTrackListUi(album: Album)
    }

    interface Presenter : BasePresenter<View> {
        fun listAlbums()
        fun openTrackList(album: Album)
    }
}