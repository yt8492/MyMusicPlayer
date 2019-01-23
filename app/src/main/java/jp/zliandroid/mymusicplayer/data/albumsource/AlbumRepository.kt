package jp.zliandroid.mymusicplayer.data.albumsource

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import jp.zliandroid.mymusicplayer.data.Album

class AlbumRepository(private val context: Context) : AlbumDataSource {
    override fun getAlbums(callback: AlbumDataSource.LoadAlbumsCallback) {
        val albumList = arrayListOf<Album>()
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                COLUMNS,
                null,
                null,
                "ALBUM ASC"
        ).use { cursor ->
            while (cursor.moveToNext()) {
                albumList.add(createAlbum(cursor))
            }
        }
        callback.onAlbumsLoaded(albumList)
    }

    override fun getAlbum(albumId: Long, callback: AlbumDataSource.GetAlbumCallback) {
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Albums._ID + "= ?",
                arrayOf(albumId.toString()),
                null
        ).use { cursor ->
            val album = createAlbum(cursor)
            callback.onAlbumLoaded(album)
        }
    }

    private fun createAlbum(cursor: Cursor): Album {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
        val albumArtUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))?.let {
            Uri.parse(it)
        }
        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
        return Album(id, title, albumArtUri, artistName)
    }

    companion object {
        private val COLUMNS = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST
        )
    }
}