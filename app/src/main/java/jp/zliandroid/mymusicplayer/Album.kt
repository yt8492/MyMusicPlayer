package jp.zliandroid.mymusicplayer

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import java.io.Serializable
import kotlin.properties.Delegates

class Album: Serializable {

    var id: Long by Delegates.notNull()
    var album: String
    var albumArt: Uri?
    var albumId: Long by Delegates.notNull()
    var albumKey: String
    var artist: String?
    var tracks: Int by Delegates.notNull()

    constructor(cursor: Cursor){
        id          = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
        album       = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
        //albumArt    = Uri.parse( cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)))
        albumArt = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))?.let {
            Uri.parse(it)
        } ?: null
        albumId     = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Media._ID))
        albumKey    = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY))
        artist      = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
        tracks      = cursor.getInt(    cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
    }

    companion object {
        private val FILLED_PROJECTION: Array<String> = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS)

        fun getItems(activity: Context): List<Album>{
            val albums = mutableListOf<Album>()
            val resolver = activity.contentResolver
            val cursor = resolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    Album.FILLED_PROJECTION,
                    null,
                    null,
                    "ALBUM ASC"
            )

            while (cursor.moveToNext()){
                albums.add(Album(cursor))
            }

            return albums
        }

        fun getArtByAlbumId(context: Context,albumId: Long):Uri?{
            val resolver = context.contentResolver
            val SELECTION_ARG = arrayOf("")
            SELECTION_ARG[0] = albumId.toString()
            val cursor = resolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    Album.FILLED_PROJECTION,
                    MediaStore.Audio.Albums._ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            cursor.moveToFirst()
            val album = Album(cursor)
            cursor.close()
            return album.albumArt
        }

        fun getAlbumByAlbumId(context: Context, albumId: Long):Album{
            val resolver = context.contentResolver
            val SELECTION_ARG = arrayOf("")
            SELECTION_ARG[0] = albumId.toString()
            val cursor = resolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    Album.FILLED_PROJECTION,
                    MediaStore.Audio.Albums._ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            cursor.moveToFirst()
            val album = Album(cursor)
            cursor.close()
            return album
        }
    }

}