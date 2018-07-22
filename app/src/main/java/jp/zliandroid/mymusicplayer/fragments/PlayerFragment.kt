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
import jp.zliandroid.mymusicplayer.Album

import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.service.MusicPlayService
import kotlinx.android.synthetic.main.fragment_player.*
import java.text.FieldPosition

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PlayerFragment.PlayerFragmentListener] interface
 * to handle interaction events.
 * Use the [PlayerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PlayerFragment : Fragment() {
    private var listener: PlayerFragmentListener? = null
    private lateinit var track: Track
    private lateinit var receiver: MyReceiver
    private  lateinit var album: Album

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            context?.let {
                receiver = MyReceiver()
                val intentFilter = IntentFilter()
                intentFilter.addAction(MusicPlayService.ACTION_SET_PARAMS)
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
        fun onFragmentInteraction(uri: Uri)
    }

    private fun setParams(){
        music_title.text = track.title
        music_artist.text = track.artist
        music_album_title.text = track.album
        album.albumArt?.let {
            music_album_art.setImageURI(it)
        }
        meter_total.base = SystemClock.elapsedRealtime() - track.duration
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("debug", "receive")
            if (intent.action.equals(MusicPlayService.ACTION_SET_PARAMS)) {
                val trackId = intent.getLongExtra("trackId", -1)
                Log.d("debug", "trackId = $trackId")
                track = Track.getItemByTrackId(context, trackId)
                setParams()
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
