package jp.zliandroid.mymusicplayer.fragments

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.data.AlbumManager
import jp.zliandroid.mymusicplayer.data.Track
import jp.zliandroid.mymusicplayer.data.TrackManager
import jp.zliandroid.mymusicplayer.service.MusicPlayService

import kotlinx.android.synthetic.main.fragment_player.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlayerFragment.PlayerFragmentListener] interface
 * to handle interaction events.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class PlayerFragment : Fragment(), SeekBar.OnSeekBarChangeListener, View.OnClickListener, Runnable {

    private var listener: PlayerFragmentListener? = null
    private lateinit var track: Track
    private lateinit var receiver: MusicReceiver
    private  lateinit var album: Album
    private var playing = false
    private var running = false
    private var thread: Thread? = null
    private lateinit var intentFilter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("debug", "onCreate")
        arguments?.let { args ->
            context?.let {
                receiver = MusicReceiver()
                intentFilter = IntentFilter()
                intentFilter.addAction(MusicPlayService.ACTION_SET_PARAMS)
                intentFilter.addAction(MusicPlayService.ACTION_SET_CURRENT_POSITION)
                //it.registerReceiver(receiver, intentFilter)

                val albumId = args.getLong("albumId")
                album = AlbumManager.getAlbumByAlbumId(it, albumId)
                val position = args.getInt("position")
                val tracks = TrackManager.getItemsByAlbumId(it, albumId)
                track = tracks[position]
                running = true
            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("debug", "onViewCreated")
        setParams()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is PlayerFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement PlayerFragmentListener")
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("debug", "onResume")
        playing = true
        running = true
        context?.registerReceiver(receiver, intentFilter)
        thread = Thread(this)
        thread?.start()
    }

    override fun onPause() {
        super.onPause()
        Log.d("debug", "onPause")
        playing = false
        running = false
        thread = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("debug", "onDetach")
        playing = false
        running = false
        listener = null
        context?.unregisterReceiver(receiver)
    }

    interface PlayerFragmentListener {
        fun onButtonClick(controlType: Int)
    }

    override fun run() {
        //Log.d("debug", "running")
        while (running){
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            if (playing){
                //Log.d("debug", "playing")
                handler.sendMessage(Message.obtain(handler, 100))
            }
        }
    }

    private val handler = @SuppressLint("HandlerLeak")
    object: Handler(){
        override fun handleMessage(msg: Message?) {
            val msec = msg?.let { it.what } ?:0
            incrementPosition(msec)
        }
    }


    private fun incrementPosition(msec: Int){
        seek_bar.progress += msec
        meter_now.base = SystemClock.elapsedRealtime() - seek_bar.progress
    }

    private fun setParams(){
        playing = true
        Log.d("debug", "trackTitle = ${track.title}")
        music_title.text = track.title
        music_artist.text = track.artist
        music_album_title.text = track.album
        music_title.isSelected = true
        album.albumArt?.let {
            music_album_art.setImageURI(it)
        }
        meter_total.base = SystemClock.elapsedRealtime() - track.duration
        seek_bar.progress = 0
        seek_bar.max = track.duration.toInt()
        seek_bar.setOnSeekBarChangeListener(this)
        if (playing){
            music_play.setImageResource(R.drawable.stop)
        } else {
            music_play.setImageResource(R.drawable.start)
        }
        music_play.setOnClickListener(this)
        music_previous.setOnClickListener(this)
        music_next.setOnClickListener(this)
    }

    private fun setCurrentPosition(currentPosition: Int){
        if (playing) {
            music_play.setImageResource(R.drawable.stop)
        } else {
            music_play.setImageResource(R.drawable.start)
        }
        seek_bar.progress = currentPosition
        meter_now.base = SystemClock.elapsedRealtime() - currentPosition
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        meter_now.base = SystemClock.elapsedRealtime() - progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        /*val broadcastIntent = Intent()
        broadcastIntent.action = ACTION_SEND_CONTROL
        broadcastIntent.putExtra("type", MUSIC_STOP)
        sendBroadcast(broadcastIntent)*/
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.let {
            val currentPosition = it.progress
            //meter_now.base = SystemClock.elapsedRealtime() - currentPosition
            val broadcastIntent = Intent()
            broadcastIntent.action = ACTION_CHANGE_SEEKBAR
            broadcastIntent.putExtra("currentPosition", currentPosition)
            sendBroadcast(broadcastIntent)
        }

    }

    private fun sendBroadcast(intent: Intent){
        context?.let {
            it.sendBroadcast(intent)
        }
    }

    override fun onClick(v: View?) {
        v?.let { view ->
            listener?.let { clickListener ->
                when(view){
                    music_play ->
                        if (playing){
                            music_play.setImageResource(R.drawable.start)
                            clickListener.onButtonClick(MUSIC_STOP)
                            playing = false
                        } else {
                            music_play.setImageResource(R.drawable.stop)
                            clickListener.onButtonClick(MUSIC_START)
                            playing = true
                        }

                    music_previous -> {
                        clickListener.onButtonClick(MUSIC_BACK)
                        //running = false
                    }

                    music_next -> {
                        clickListener.onButtonClick(MUSIC_NEXT)
                        //running = false
                    }
                }
            }
        }
    }

    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Log.d("debug", "receive")
            when (intent.action){
                MusicPlayService.ACTION_SET_PARAMS -> {
                    val trackId = intent.getLongExtra("trackId", -1)
                    Log.d("debug", "trackId = $trackId")
                    track = TrackManager.getItemByTrackId(context, trackId)
                    album = AlbumManager.getAlbumByAlbumId(context, track.albumId)
                    setParams()
                    running = true
                }
                MusicPlayService.ACTION_SET_CURRENT_POSITION -> {
                    val currentPosition = intent.getIntExtra("currentPosition",-1)
                    playing = intent.getBooleanExtra("playing",false)
                    //Log.d("debug", "position = $currentPosition")
                    setCurrentPosition(currentPosition)
                }
            }
        }

    }

    companion object {

        const val NAME = "PlayerFragment"
        const val MUSIC_START = 1
        const val MUSIC_STOP = 2
        const val MUSIC_BACK = 3
        const val MUSIC_NEXT = 4
        const val ACTION_CHANGE_SEEKBAR = "android.intent.action.CHANGE_SEEKBAR"

        @JvmStatic
        fun newInstance(albumId: Long, position: Int) =
                PlayerFragment().apply {
                    arguments = Bundle().apply {
                        putLong("albumId", albumId)
                        putInt("position", position)
                    }
        }
    }
}
