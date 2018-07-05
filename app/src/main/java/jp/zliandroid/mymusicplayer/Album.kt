package jp.zliandroid.mymusicplayer

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import kotlin.properties.Delegates

class Album {

    var id: Long by Delegates.notNull()
    lateinit var album: String
    var albumArt: String?
    var albumId: Long by Delegates.notNull()
    lateinit var albumKey: String
    lateinit var artist: String
    var tracks: Int by Delegates.notNull()

    constructor(cursor: Cursor){
        id          = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
        album       = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM))
        albumArt    = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
        albumId     = cursor.getLong(   cursor.getColumnIndex(MediaStore.Audio.Media._ID))
        albumKey    = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY))
        artist      = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
        tracks      = cursor.getInt(    cursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS))
    }

    companion object {
        val FILLED_PROJECTION: Array<String> = arrayOf(
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

        fun getArtByAlbumId(activity: Context,albumId: Long):String?{
            val resolver = activity.contentResolver
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
    }

}