package jp.zliandroid.mymusicplayer.data

import android.net.Uri

data class Track(val id: String, val title: String, val albumId: Long, val uri: Uri, val artistName: String?, val duration: Long)