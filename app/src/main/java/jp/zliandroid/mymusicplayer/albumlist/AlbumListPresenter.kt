package jp.zliandroid.mymusicplayer.albumlist

import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import javax.inject.Inject

class AlbumListPresenter @Inject constructor(
        private val albumRepository: AlbumRepository
) : AlbumListContract.Presenter {

    private lateinit var albumListView: AlbumListContract.View

    override fun takeView(view: AlbumListContract.View) {
        albumListView = view.apply {
            presenter = this@AlbumListPresenter
        }
    }

    override fun start() {
        listAlbums()
    }

    override fun listAlbums() {
        val albums = albumRepository.getAlbums()
        if (albumListView.isActive) {
            albumListView.showAlbums(albums)
        }
    }

    override fun openTrackList(album: Album) {
        albumListView.showTrackListUi(album)
    }
}