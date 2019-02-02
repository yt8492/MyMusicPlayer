package jp.zliandroid.mymusicplayer.tracklist


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.data.Track
import jp.zliandroid.mymusicplayer.musicplay.MusicPlayActivity
import kotlinx.android.synthetic.main.fragment_track_list.*
import kotlinx.android.synthetic.main.fragment_track_list.view.*
import kotlinx.android.synthetic.main.item_track.view.*

class TrackListFragment : Fragment(), TrackListContract.View {
    override var isActive = false
        get() = isAdded

    override lateinit var presenter: TrackListContract.Presenter

    private val itemListener = object : TrackItemListener {
        override fun onTrackClick(trackList: List<Track>, position: Int) {
            val albumId = arguments?.getLong(ARGUMENT_ALBUM_ID) ?: -1
            presenter.openTrack(albumId, trackList, position)
        }
    }

    private val listAdapter = TrackListAdapter(listOf(), itemListener)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_list, container, false).apply {
            track_list.adapter = listAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showAlbumInfo(album: Album) {
        if (album.albumArtUri != null) {
            album_art.setImageURI(album.albumArtUri)
        } else {
            album_art.setImageResource(R.drawable.dummy_album_art_slim)
        }
        album_title.text = album.title
        track_count.text = "${album.trackCnt}曲"
    }

    override fun showTracks(tracks: List<Track>) {
        listAdapter.tracks = tracks
    }

    override fun showMusicPlayUi(albumId: Long, tracks: List<Track>, position: Int) {
        val intent = Intent(context, MusicPlayActivity::class.java).apply {
            putExtra("AlbumId" ,albumId)
            putExtra("TrackIds", tracks.map { it.id }.toLongArray())
            putExtra("TrackPosition", position)
        }
        startActivity(intent)
    }

    private class TrackListAdapter(tracks: List<Track>, private val itemListener: TrackItemListener) : BaseAdapter() {
        var tracks: List<Track> = tracks
            set(values) {
                field = values
                notifyDataSetChanged()
            }

        override fun getItem(position: Int) = tracks[position]

        override fun getItemId(position: Int) = tracks[position].id

        override fun getCount() = tracks.size

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val track = getItem(position)
            val rowView = convertView ?: LayoutInflater.from(parent?.context)
                    .inflate(R.layout.item_track, parent, false)
            with(rowView) {
                track_title.text = track.title
                track_artist.text = track.artistName
                val dm = track.duration / 60000
                val ds = (track.duration - dm * 60000) / 1000
                track_duration.text = "%02d:%02d".format(dm, ds)

                setOnClickListener {
                    itemListener.onTrackClick(tracks, position)
                }
            }

            return rowView
        }
    }

    interface TrackItemListener {
        fun onTrackClick(trackList: List<Track>, position: Int)
    }

    companion object {
        private val ARGUMENT_ALBUM_ID = "ALBUM_ID"

        @JvmStatic
        fun newInstance(albumId: Long) = TrackListFragment().apply {
            arguments = Bundle().apply {
                putLong(ARGUMENT_ALBUM_ID, albumId)
            }
        }
    }

}
