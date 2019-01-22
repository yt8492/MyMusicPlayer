package jp.zliandroid.mymusicplayer.musicplay

interface MusicPlayController {
    fun start()
    fun stop()
    fun pause()
    fun resume()
    fun seekTo(milliSec: Long)
    fun playPrevious()
    fun playNext()
}