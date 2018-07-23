package jp.zliandroid.mymusicplayer.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar

import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.service.MusicPlayService

import kotlinx.android.synthetic.main.fragment_player.*
import java.net.MalformedURLException

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlayerFragment.PlayerFragmentListener] interface
 * to handle interaction events.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerFragment : Fragment(), SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private var listener: PlayerFragmentListener? = null
    private lateinit var track: Track
    private lateinit var receiver: MusicReceiver
    private  lateinit var album: Album
    private var playing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            context?.let {
                receiver = MusicReceiver()
                val intentFilter = IntentFilter()
                intentFilter.addAction(MusicPlayService.ACTION_SET_PARAMS)
                intentFilter.addAction(MusicPlayService.ACTION_SET_CURRENT_POSITION)
                it.registerReceiver(receiver, intentFilter)

                val albumId = args.getLong("albumId")
                album = Album.getAlbumByAlbumId(it, albumId)
                val position = args.getInt("position")
                val tracks = Track.getItemsByAlbumId(it, albumId)
                track = tracks[position]
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

    override fun onDetach() {
        super.onDetach()
        context?.let {
            it.unregisterReceiver(receiver)
        }
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface PlayerFragmentListener {
        fun onButtonClick(controlType: Int)
    }

    private fun setParams(){
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
        if (playing) {
            music_play.setImageResource(R.drawable.start)
        } else {
            music_play.setImageResource(R.drawable.stop)
        }
        music_play.setOnClickListener(this)
        music_previous.setOnClickListener(this)
        music_next.setOnClickListener(this)
    }

    private fun setCurrentPosition(currentPosition: Int){
        seek_bar.progress = currentPosition
        meter_now.base = SystemClock.elapsedRealtime() - currentPosition
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

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
                        } else {
                            music_play.setImageResource(R.drawable.stop)
                            clickListener.onButtonClick(MUSIC_START)
                        }

                    music_previous ->
                        clickListener.onButtonClick(MUSIC_BACK)

                    music_next ->
                        clickListener.onButtonClick(MUSIC_NEXT)
                }
            }
        }
    }

    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Log.d("debug", "receive")
            if (intent.action.equals(MusicPlayService.ACTION_SET_PARAMS)) {
                val trackId = intent.getLongExtra("trackId", -1)
                Log.d("debug", "trackId = $trackId")
                track = Track.getItemByTrackId(context, trackId)
                setParams()
            } else if (intent.action.equals(MusicPlayService.ACTION_SET_CURRENT_POSITION)){
                val currentPosition = intent.getIntExtra("currentPosition",-1)
                playing = intent.getBooleanExtra("playing",false)
                //Log.d("debug", "position = $currentPosition")
                setCurrentPosition(currentPosition)
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlayerFragment.
         */
        // TODO: Rename and change types and number of parameters
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
