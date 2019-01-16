package jp.zliandroid.mymusicplayer.data

import android.net.Uri

data class Album(val id: Long, val title: String, val albumArtUri: Uri?, val artistName: String?, val trackIds: List<Long>)