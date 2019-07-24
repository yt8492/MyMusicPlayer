package jp.zliandroid.mymusicplayer.musicplay

import jp.zliandroid.mymusicplayer.BasePresenter
import jp.zliandroid.mymusicplayer.BaseView
import jp.zliandroid.mymusicplayer.data.Album
import jp.zliandroid.mymusicplayer.data.Track

interface MusicPlayContract {
    interface View : BaseView<Presenter> {
        val isActive: Boolean

        fun setTrackInfo(track: Track, album: Album)
        fun playStart(track: Track)
        fun playStop()
        fun playPause()
        fun playResume()
        fun seekTo(milliSec: Long)
        fun finish()
    }

    interface Presenter : BasePresenter<View> {
        fun showTrackInfo(track: Track)
        fun playStart(track: Track)
        fun playStop()
        fun playPause()
        fun playResume()
        fun seekTo(milliSec: Long)
        fun playNext()
        fun playPrev()
    }
}