package jp.zliandroid.mymusicplayer.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import jp.zliandroid.mymusicplayer.Track

class MusicPlayService : Service(), MediaPlayer.OnCompletionListener {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var tracks: List<Track>
    private var nowPosition = 0
    private var alreadyPlayed = false

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val albumId = it.getLongExtra("albumId", -1)
            nowPosition = it.getIntExtra("position",-1)
            Toast.makeText(this, "albumId = $albumId, position = $nowPosition", Toast.LENGTH_SHORT).show()
            tracks = Track.getItemsByAlbumId(this,albumId)
            if (alreadyPlayed){
                mediaPlayer.run {
                    reset()
                    release()
                }
            }
            mediaPlayer = MediaPlayer.create(this, tracks[nowPosition].uri)
            alreadyPlayed = true
            mediaPlayer.start()
            Log.d("debug","start playing")
            if (nowPosition < tracks.size + 1){
               //mediaPlayer.setNextMediaPlayer(MediaPlayer.create(this, tracks[++nowPosition].uri))
            }
            mediaPlayer.setOnCompletionListener(this)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCompletion(p0: MediaPlayer?) {
        if(nowPosition < tracks.size + 1){
            Log.d("debug","completed")
            mediaPlayer = MediaPlayer.create(this, tracks[++nowPosition].uri)
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener(this)
        }

    }

    companion object {
        const val SERVICE_ACTION = "MusicPlayAction"
    }
}
