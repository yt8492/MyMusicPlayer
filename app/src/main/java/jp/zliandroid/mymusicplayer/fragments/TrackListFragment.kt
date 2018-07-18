package jp.zliandroid.mymusicplayer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.adapter.MyTrackRecyclerViewAdapter

import jp.zliandroid.mymusicplayer.fragments.dummy.DummyContent
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.android.synthetic.main.fragment_track_list.view.*

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TrackListFragment.FragmentListener] interface.
 */
class TrackListFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var album: Album
    private var listener: TrackListFragmentListener? = null
    private var backListener: BackListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            args ->
            columnCount = args.getInt(ARG_COLUMN_COUNT)
            context?.let {
                val albumId = args.getLong("albumId")
                album = Album.getAlbumByAlbumId(it ,albumId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_track_list, container, false)

        Log.d("debug","albumId = ${album.albumId}")
        // Set the adapter
        if (view is LinearLayout){
            with(view){
                list.layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                Log.d("debug","set adapter")
                val trackList = Track.getItemsByAlbumId(context,album.albumId)
                trackList.forEach {
                    Log.d("debug",it.title)
                }
                list.adapter = MyTrackRecyclerViewAdapter(trackList, listener)
                val dividerItemDecoration = DividerItemDecoration(context,LinearLayoutManager(activity).orientation)
                list.addItemDecoration(dividerItemDecoration)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        album.albumArt?.let {
            album_title_art.setImageURI(it)
        } ?: run {
            album_title_art.setImageResource(R.drawable.dummy_album_art_slim)
        }
        album_title.text = album.album
        track_count.text = "${album.tracks} tracks"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TrackListFragmentListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
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
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface TrackListFragmentListener{
        fun onClickListItem(track: Track)
    }

    interface BackListener{
        fun onBack()
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(albumId: Long) =
                TrackListFragment().apply {
                    arguments = Bundle().apply {
                        putLong("albumId", albumId)
                    }
                }
    }
}
