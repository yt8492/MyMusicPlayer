package jp.zliandroid.mymusicplayer.data

import android.net.Uri

class Track(

    val id: Long,
    val albumId: Long,
    val artistId: Long,
    val path: String,
    val title: String,
    val album: String,
    val artist: String,
    val uri: Uri,
    val duration: Long,
    val trackNo: Int
)