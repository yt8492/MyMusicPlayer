package jp.zliandroid.mymusicplayer.data

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

class AlbumManager {
    companion object {
        private val FILLED_PROJECTION: Array<String> = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS)

        private fun createAlbum(cursor: Cursor): Album {
            val id          = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
            val album       = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
            val albumArt    = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))?.let {
                Uri.parse(it)
            }
            val albumId     = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Media._ID))
            val albumKey    = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY))
            val artist      = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)) ?: null
            val tracks      = cursor.getInt(    cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
            return Album(id, album, albumArt, albumId, albumKey, artist, tracks)
        }

        fun getItems(activity: Context): List<Album>{
            val albums = mutableListOf<Album>()
            val resolver = activity.contentResolver
            val cursor = resolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    FILLED_PROJECTION,
                    null,
                    null,
                    "ALBUM ASC"
            )

            while (cursor.moveToNext()){
                albums.add(createAlbum(cursor))
            }

            return albums
        }

        fun getAlbumByAlbumId(context: Context, albumId: Long): Album {
            val resolver = context.contentResolver
            val SELECTION_ARG = arrayOf("")
            SELECTION_ARG[0] = albumId.toString()
            val cursor = resolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    FILLED_PROJECTION,
                    MediaStore.Audio.Albums._ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            cursor.moveToFirst()
            val album = createAlbum(cursor)
            cursor.close()
            return album
        }
    }
}