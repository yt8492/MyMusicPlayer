package jp.zliandroid.mymusicplayer.albumlist

import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumDataSource
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository

class AlbumListPresenter(private val albumRepository: AlbumRepository, private val albumListView: AlbumListContract.View)
    : AlbumListContract.Presenter {
    init {
        albumListView.presenter = this
    }

    override fun start() {
        listAlbums()
    }

    override fun listAlbums() {
        albumRepository.getAlbums(object : AlbumDataSource.LoadAlbumsCallback {
            override fun onAlbumsLoaded(albums: List<Album>) {
                if (albumListView.isActive) {
                    albumListView.showAlbums(albums)
                }
            }
        })
    }

    override fun openTrackList(album: Album) {
        albumListView.showTrackListUi(album)
    }
}