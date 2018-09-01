package jp.zliandroid.mymusicplayer.data

import android.net.Uri

data class Album(
        val id: Long,
        val album: String,
        val albumArt: Uri?,
        val albumId: Long,
        val albumKey: String,
        val artist: String?,
        val tracks: Int
)