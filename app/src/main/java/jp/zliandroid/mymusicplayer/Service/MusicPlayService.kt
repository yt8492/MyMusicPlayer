package jp.zliandroid.mymusicplayer.Service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import jp.zliandroid.mymusicplayer.Track

class MusicPlayService : Service() {

    lateinit var mediaPlayer: MediaPlayer
    lateinit var tracks: List<Track>

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val albumId = it.getLongExtra("albumId", -1)
            val position = it.getIntExtra("position",-1)
            Toast.makeText(this, "albumId = $albumId, position = $position", Toast.LENGTH_SHORT).show()
            tracks = Track.getItemsByAlbumId(this,albumId)
            mediaPlayer = MediaPlayer.create(this, tracks[position].uri)
            mediaPlayer.start()
        }
        return super.onStartCommand(intent, flags, startId)
    }
}
