package jp.zliandroid.mymusicplayer.data.albumsource

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import jp.zliandroid.mymusicplayer.data.Album
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepository @Inject constructor(private val context: Context) : AlbumDataSource {
    override fun getAlbums(): List<Album> {
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
        return albumList
    }

    override fun getAlbum(albumId: Long): Album {
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Albums._ID + "= ?",
                arrayOf(albumId.toString()),
                null
        ).use { cursor ->
            cursor.moveToFirst()
            return createAlbum(cursor)
        }
    }

    override fun getAlbumByTrackAlbumId(albumId: Long): Album {
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Albums.ALBUM_ID + "= ?",
                arrayOf(albumId.toString()),
                null
        ).use { cursor ->
            cursor.moveToFirst()
            return createAlbum(cursor)
        }
    }

    private fun createAlbum(cursor: Cursor): Album {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
        val albumArtUri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))?.let {
            Uri.parse(it)
        }
        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
        val trackCnt = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
        return Album(id, title, albumArtUri, artistName, trackCnt)
    }

    companion object {
        private val COLUMNS = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        )
    }
}