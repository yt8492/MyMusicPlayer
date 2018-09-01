package jp.zliandroid.mymusicplayer.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore

class TrackManager {

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

        fun createTrack(cursor: Cursor): Track {
            val id              = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media._ID ))
            val albumId         = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM_ID ))
            val artistId        = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST_ID ))
            val path            = cursor.getString( cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            val title           = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.TITLE ))
            val album           = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM ))
            val artist          = cursor.getString( cursor.getColumnIndex( MediaStore.Audio.Media.ARTIST ))
            val uri             = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
            val duration        = cursor.getLong( cursor.getColumnIndex( MediaStore.Audio.Media.DURATION ))
            val trackNo         = cursor.getInt( cursor.getColumnIndex( MediaStore.Audio.Media.TRACK ))
            return Track(id, albumId, artistId, path, title, album, artist, uri, duration, trackNo)
        }

        fun getItems(context: Context):List<Track>{
            val tracks: ArrayList<Track> = arrayListOf()
            val resolver: ContentResolver = context.contentResolver
            val cursor: Cursor = resolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    COLUMNS,
                    null,
                    null,
                    null
            )
            while (cursor.moveToNext()){
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 3000){
                    continue
                }
                tracks.add(TrackManager.createTrack(cursor))
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
                    COLUMNS,
                    MediaStore.Audio.Media.ALBUM_ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            while (cursor.moveToNext()){
                if (cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)) < 3000){
                    continue
                }
                tracks.add(TrackManager.createTrack(cursor))
            }
            cursor.close()
            return tracks
        }

        fun getItemByTrackId(context: Context, trackId: Long): Track {
            val resolver = context.contentResolver
            val SELECTION_ARG: Array<String> = arrayOf("")
            SELECTION_ARG[0] = trackId.toString()
            val cursor: Cursor = resolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    COLUMNS,
                    MediaStore.Audio.Media._ID + "= ?",
                    SELECTION_ARG,
                    null
            )
            cursor.moveToFirst()
            val track = TrackManager.createTrack(cursor)
            cursor.close()
            return track
        }

    }

}