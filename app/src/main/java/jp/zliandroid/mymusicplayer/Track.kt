package jp.zliandroid.mymusicplayer

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.DialogTitle
import java.io.Serializable
import kotlin.properties.Delegates

class Track : Serializable {

    var id: Long by Delegates.notNull()
    var albumId: Long by Delegates.notNull()
    var artistId: Long by Delegates.notNull()
    lateinit var path: String
    lateinit var title: String
    lateinit var album: String
    lateinit var artist: String
    lateinit var uri: Uri
    var duration: Long by Delegates.notNull()
    var trackNo: Int by Delegates.notNull()

    constructor(cursor: Cursor){
        id              = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media._ID ))
        path            = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
        title           = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.TITLE ))
        album           = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM ))
        artist          = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST ))
        albumId         = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM_ID ))
        artistId        = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST_ID ))
        duration        = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.DURATION ))
        trackNo         = cursor.getInt( cursor.getColumnIndex( MediaStore.Audio.Media.TRACK ))
        uri             = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
    }

    companion object {
        val COLUMNS: Array<String> = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TRACK
        )

        fun getItems(context: Context):List<Track>{
            val tracks: ArrayList<Track> = arrayListOf()
            val resolver: ContentResolver = context.contentResolver
            val cursor: Cursor = resolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Track.COLUMNS,
                    null,
                    null,
                    null
            )
            while (cursor.moveToNext()){
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 3000){
                    continue
                }
                tracks.add(Track(cursor))
            }
            cursor.close()
            return tracks
        }

        fun getItemsByAlbumId(context: Context, albumId: Long): List<Track>{
            val tracks: ArrayList<Track> = arrayListOf()
            val resolver: ContentResolver = context.contentResolver
            val SELECTION_ARG: Array<String> = arrayOf("")
            SELECTION_ARG[0] = albumId.toString()
            val cursor: Cursor = resolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Track.COLUMNS,
                    MediaStore.Audio.Media.ALBUM_ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            while (cursor.moveToNext()){
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 3000){
                    continue
                }
                tracks.add(Track(cursor))
            }
            cursor.close()
            return tracks
        }

    }

}