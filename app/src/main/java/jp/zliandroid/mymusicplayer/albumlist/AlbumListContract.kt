package jp.zliandroid.mymusicplayer.albumlist

import jp.zliandroid.mymusicplayer.BasePresenter
import jp.zliandroid.mymusicplayer.BaseView

interface AlbumListContract {
    interface View: BaseView<Presenter> {
        var isActive: Boolean

        fun showAlbums(albumIds: List<Long>)
        fun showTrackListUi(albumId: Long)
    }

    interface Presenter: BasePresenter {
        fun listAlbums()
        fun openTrackList(albumId: Long)
    }
}