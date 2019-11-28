package jp.zliandroid.mymusicplayer.albumlist


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection

import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.tracklist.TrackListActivity
import kotlinx.android.synthetic.main.fragment_album_list.view.*
import kotlinx.android.synthetic.main.item_album.view.*
import javax.inject.Inject

class AlbumListFragment : Fragment(), AlbumListContract.View {
    override var isActive = false
        get() = isAdded

    @Inject
    override lateinit var presenter: AlbumListContract.Presenter

    private var itemListener = object : AlbumItemListener {
        override fun onAlbumClick(album: Album) {
            presenter.openTrackList(album)
        }
    }

    private val listAdapter = AlbumListAdapter(listOf(), itemListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album_list, container, false).apply {
            album_list.adapter = listAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
        presenter.start()
    }

    override fun showAlbums(albums: List<Album>) {
        listAdapter.albums = albums
    }

    override fun showTrackListUi(album: Album) {
        val intent = Intent(context, TrackListActivity::class.java).apply {
            putExtra("AlbumId", album.id)
        }
        startActivity(intent)
    }

    private class AlbumListAdapter(albums: List<Album>, private val itemListener: AlbumItemListener) : BaseAdapter() {
        var albums: List<Album> = albums
            set(values) {
                field = values
                notifyDataSetChanged()
            }

        override fun getItem(position: Int) = albums[position]

        override fun getItemId(position: Int) = albums[position].id

        override fun getCount() = albums.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val album = getItem(position)
            val rowView = convertView ?: LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_album, parent, false)
            with(rowView) {
                album_title.text = album.title
                artist.text = album.artistName
                album.albumArtUri?.let {
                    album_art.setImageURI(it)
                } ?: album_art.setImageResource(R.drawable.dummy_album_art_slim)
                setOnClickListener {
                    itemListener.onAlbumClick(album)
                }
            }
            return rowView
        }
    }

    interface AlbumItemListener {
        fun onAlbumClick(album: Album)
    }

    companion object {
        @JvmStatic
        fun newInstance() = AlbumListFragment()
    }
}
