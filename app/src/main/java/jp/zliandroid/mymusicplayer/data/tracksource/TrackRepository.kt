package jp.zliandroid.mymusicplayer.data.tracksource

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import jp.zliandroid.mymusicplayer.data.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepository @Inject constructor(private val context: Context) : TrackDataSource {
    override fun getTracks(albumId: Long): List<Track> {
        val trackList = arrayListOf<Track>()
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Media.ALBUM_ID + "= ?",
                arrayOf(albumId.toString()),
                null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                trackList.add(createTrack(cursor))
            }
        }
        return trackList
    }

    override fun getTracks(trackIds: List<Long>): List<Track> {
        val trackList = arrayListOf<Track>()
        trackIds.forEach {
            trackList.add(getTrack(it))
        }
        return trackList
    }

    override fun getTrack(trackId: Long): Track {
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Media._ID + "= ?",
                arrayOf(trackId.toString()),
                null
        ).use { cursor ->
            cursor.moveToFirst()
            return createTrack(cursor)
        }
    }

    private fun createTrack(cursor: Cursor): Track {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
        val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
        val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        val artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)) ?: null
        val duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
        return Track(id, title, albumId, uri, artistName, duration)
    }

    companion object {
        private val COLUMNS = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION
        )
    }
}