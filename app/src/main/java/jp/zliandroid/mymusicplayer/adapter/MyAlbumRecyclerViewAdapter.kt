package jp.zliandroid.mymusicplayer.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R

import jp.zliandroid.mymusicplayer.fragments.AlbumListFragment.AlbumListFragmentListener
import jp.zliandroid.mymusicplayer.fragments.dummy.DummyContent.DummyItem

import kotlinx.android.synthetic.main.fragment_album.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [AlbumListFragmentListener].
 */
class MyAlbumRecyclerViewAdapter(
        private val mValues: List<Album>,
        private val mListener: AlbumListFragmentListener?)
    : RecyclerView.Adapter<MyAlbumRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Album
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onClickListItem(item.albumId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_album, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        item.albumArt?.let {
            holder.albumArt.setImageURI(it)
        } ?: run{
            holder.albumArt.setImageResource(R.drawable.dummy_album_art_slim)
        }
        holder.albumTile.text = item.album
        holder.artist.text = item.artist

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val albumArt: ImageView = mView.album_art
        val albumTile: TextView = mView.album_title
        val artist: TextView = mView.artist

        override fun toString(): String {
            return super.toString() + " '" + albumTile.text + "'"
        }
    }
}
