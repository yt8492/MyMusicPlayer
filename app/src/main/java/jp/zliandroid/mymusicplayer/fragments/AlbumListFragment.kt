package jp.zliandroid.mymusicplayer.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.adapter.MyAlbumRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_album_list.*


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [AlbumListFragment.FragmentListener] interface.
 */
class AlbumListFragment : Fragment() {

    private var columnCount = 1

    private var listener: AlbumListFragmentListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_album_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                Log.d("debug","set adapter")
                adapter = MyAlbumRecyclerViewAdapter(Album.getItems(context), listener)
                val dividerItemDecoration = DividerItemDecoration(context,LinearLayoutManager(activity).orientation)
                addItemDecoration(dividerItemDecoration)
            }

        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AlbumListFragmentListener) {
            listener = context
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
    interface AlbumListFragmentListener{
        fun onClickListItem(albumId: Long)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                AlbumListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
