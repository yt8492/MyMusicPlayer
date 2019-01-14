package jp.zliandroid.mymusicplayer.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import jp.zliandroid.mymusicplayer.data.Track
import jp.zliandroid.mymusicplayer.activity.WakeupActivity
import jp.zliandroid.mymusicplayer.data.TrackManager
import jp.zliandroid.mymusicplayer.fragments.PlayerFragment

class MusicPlayService : Service(), MediaPlayer.OnCompletionListener{

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var tracks: List<Track>
    private var nowPosition = -1
    private var alreadyPlayed = false
    private var running = false
    private lateinit var receiver: ControlReceiver

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        super.onCreate()
        receiver = ControlReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(WakeupActivity.ACTION_CONNECT_SERVICE)
        intentFilter.addAction(WakeupActivity.ACTION_SEND_CONTROL)
        intentFilter.addAction(PlayerFragment.ACTION_CHANGE_SEEKBAR)
        registerReceiver(receiver, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action){
                WakeupActivity.ACTION_CONNECT_SERVICE -> {
                    val albumId = it.getLongExtra("albumId", -1)
                    nowPosition = it.getIntExtra("position",-1)
                    //Toast.makeText(this, "albumId = $albumId, position = $nowPosition", Toast.LENGTH_SHORT).show()
                    tracks = TrackManager.getItemsByAlbumId(this,albumId)
                    if (alreadyPlayed) {
                        stopPlayer()
                    }
                    startPlayer()
                }
                WakeupActivity.ACTION_SEND_CONTROL -> {
                    Log.d("debug", "receive control")
                    val controlType = it.getIntExtra("type", -1)
                    when (controlType) {
                        PlayerFragment.MUSIC_STOP -> playPause()
                        PlayerFragment.MUSIC_START -> playStart()
                        PlayerFragment.MUSIC_BACK -> playerBack()
                        PlayerFragment.MUSIC_NEXT -> playerSkip()
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCompletion(mp: MediaPlayer?) {
        playerSkip()
    }

    private fun startPlayer(){
        mediaPlayer = MediaPlayer.create(this, tracks[nowPosition].uri)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener(this)
        alreadyPlayed = true
        running = true
        sendTrack()
    }

    private fun stopPlayer(){
        mediaPlayer.reset()
        mediaPlayer.release()
        alreadyPlayed = false
        running = false
    }

    private fun playStart(){
        if (!mediaPlayer.isPlaying){
            //mediaPlayer.seekTo(mediaPlayer.currentPosition)
            mediaPlayer.start()
        }
    }

    private fun playPause(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.pause()
        }
    }

    private fun playerSkip(){
        if(nowPosition < tracks.size - 1){
            nowPosition++
            stopPlayer()
            Log.d("debug","completed")
            startPlayer()
        } else {
            mediaPlayer.seekTo(mediaPlayer.duration)
            playPause()
            sendPosition()
        }
    }

    private fun playerBack(){
        if (mediaPlayer.currentPosition < 3000) {
            if (nowPosition > 0) {
                stopPlayer()
                nowPosition--
                startPlayer()
            } else {
                mediaPlayer.seekTo(0)
                sendPosition()
            }
        } else {
            mediaPlayer.seekTo(0)
            sendPosition()
        }
        /*if (mediaPlayer.isPlaying) {

        } else {
            mediaPlayer.seekTo(0)
            sendPosition()
        }*/
    }

    /*override fun run() {
        while (running){
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            //sendPosition()
        }
    }*/

    private fun sendPosition(){
        val broadcastIntent = Intent()
        broadcastIntent.action = ACTION_SET_CURRENT_POSITION
        broadcastIntent.putExtra("currentPosition", mediaPlayer.currentPosition)
        broadcastIntent.putExtra("playing", mediaPlayer.isPlaying)
        //Log.d("debug", "send position")
        sendBroadcast(broadcastIntent)
    }

    private fun sendTrack(){
        val broadcastIntent = Intent()
        broadcastIntent.action = ACTION_SET_PARAMS
        broadcastIntent.putExtra("trackId", tracks[nowPosition].id)
        broadcastIntent.putExtra("playing", mediaPlayer.isPlaying)
        sendBroadcast(broadcastIntent)
        Log.d("debug", "send broadcast id = ${tracks[nowPosition].id}")
        //startPlayer()
    }

    inner class ControlReceiver : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let { intent ->
                if (intent.action == PlayerFragment.ACTION_CHANGE_SEEKBAR){
                    val currentPosition = intent.getIntExtra("currentPosition", -1)
                    mediaPlayer.seekTo(currentPosition)
                    sendPosition()
                }
            }
        }

    }

    companion object {
        const val ACTION_SET_PARAMS = "android.intent.action.SET_PARAMS"
        const val ACTION_SET_CURRENT_POSITION = "android.intent.action.ACTION_SET_CURRENT_POSITION"
    }
}