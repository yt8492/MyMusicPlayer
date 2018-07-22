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
            startPlayer()
            alreadyPlayed = true
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        nowPosition++
        if(nowPosition < tracks.size){
            Log.d("debug","completed")
            startPlayer()
        } else {
            mediaPlayer.release()
            alreadyPlayed = false
        }

    }

    private fun startPlayer(){
        val broadcastIntent = Intent()
        broadcastIntent.action = ACTION_SET_PARAMS
        broadcastIntent.putExtra("trackId", tracks[nowPosition].id)
        sendBroadcast(broadcastIntent)
        Log.d("debug", "send broadcast id = ${tracks[nowPosition].id}")
        mediaPlayer = MediaPlayer.create(this, tracks[nowPosition].uri)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener(this)
    }

    companion object {
        const val ACTION_SET_PARAMS = "android.intent.action.setParams"
    }
}
