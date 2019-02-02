package jp.zliandroid.mymusicplayer.data.albumsource

import jp.zliandroid.mymusicplayer.data.Album

interface AlbumDataSource {
    fun getAlbums(): List<Album>
    fun getAlbum(albumId: Long): Album
    fun getAlbumByTrackAlbumId(albumId: Long): Album
}