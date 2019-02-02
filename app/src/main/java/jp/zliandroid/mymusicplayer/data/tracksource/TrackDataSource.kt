package jp.zliandroid.mymusicplayer.data.tracksource

import jp.zliandroid.mymusicplayer.data.Track

interface TrackDataSource {
    fun getTracks(albumId: Long): List<Track>
    fun getTracks(trackIds: List<Long>): List<Track>
    fun getTrack(trackId: Long): Track
}