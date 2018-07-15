package jp.zliandroid.mymusicplayer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.adapter.MyTrackRecyclerViewAdapter

import jp.zliandroid.mymusicplayer.fragments.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [TrackListFragment.FragmentListener] interface.
 */
class TrackListFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var album: Album
    private var listener: FragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            args ->
            columnCount = args.getInt(ARG_COLUMN_COUNT)
            album = args.getSerializable("album") as Album
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_track_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyTrackRecyclerViewAdapter(Track.getItemsByAlbumId(this.context,album.albumId), listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentListener) {
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
    interface FragmentListener{
        fun onClickListItem(track: Track)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                TrackListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
