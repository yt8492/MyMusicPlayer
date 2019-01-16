package jp.zliandroid.mymusicplayer.data.tracksource

import jp.zliandroid.mymusicplayer.data.Track

interface TrackDataSource {
    interface LoadTracksCallback {
        fun onTracksLoaded(tracks: List<Track>)
    }

    interface GetTrackCallback {
        fun onTrackLoaded(track: Track)
    }

    fun getTracks(albumId: Long, callback: LoadTracksCallback)
    fun getTrack(trackId: Long, callback: GetTrackCallback)
}