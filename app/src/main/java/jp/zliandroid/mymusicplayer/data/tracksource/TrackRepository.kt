package jp.zliandroid.mymusicplayer.data.tracksource

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import jp.zliandroid.mymusicplayer.data.Track

class TrackRepository(private val context: Context) : TrackDataSource {
    override fun getTracks(albumId: Long, callback: TrackDataSource.LoadTracksCallback) {
        val trackList = arrayListOf<Track>()
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                COLUMNS,
                null,
                null,
                null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                trackList.add(createTrack(cursor))
            }
            callback.onTracksLoaded(trackList)
        }
    }

    override fun getTrack(trackId: Long, callback: TrackDataSource.GetTrackCallback) {
        val resolver = context.contentResolver
        resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                COLUMNS,
                MediaStore.Audio.Media._ID + "= ?",
                arrayOf(trackId.toString()),
                null
        ).use { cursor ->
            cursor.moveToFirst()
            val track = createTrack(cursor)
            callback.onTrackLoaded(track)
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