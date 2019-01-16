package jp.zliandroid.mymusicplayer.data.albumsource

import jp.zliandroid.mymusicplayer.data.Album

interface AlbumDataSource {
    interface LoadAlbumsCallback {
        fun onAlbumsLoaded(albums: List<Album>)
    }

    interface GetAlbumCallback {
        fun onAlbumLoaded(album: Album)
    }

    fun getAlbums(callback: LoadAlbumsCallback)
    fun getAlbum(albumId: Long, callback: GetAlbumCallback)
}