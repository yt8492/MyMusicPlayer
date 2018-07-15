package jp.zliandroid.mymusicplayer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.fragments.TrackListFragment


import jp.zliandroid.mymusicplayer.fragments.TrackListFragment.FragmentListener
import jp.zliandroid.mymusicplayer.fragments.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_track.view.*
import android.R.attr.duration



/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyTrackRecyclerViewAdapter(
        private val mValues: List<Track>,
        private val mListener: FragmentListener?)
    : RecyclerView.Adapter<MyTrackRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Track
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onClickListItem(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_track, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.trackTitle.text = item.title
        holder.trackArtist.text = item.artist
        val dm = item.duration / 60000
        val ds = (item.duration - dm * 60000) / 1000
        holder.trackDuration.text = String.format("%d:%02d", dm, ds)

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val trackTitle: TextView = mView.track_title
        val trackArtist: TextView = mView.track_artist
        val trackDuration : TextView = mView.track_duration

        override fun toString(): String {
            return super.toString() + " '" + trackTitle.text + "'"
        }
    }
}
