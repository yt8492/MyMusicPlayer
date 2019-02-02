package jp.zliandroid.mymusicplayer.musicplay

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import jp.zliandroid.mymusicplayer.data.Track

fun musicPlayPresenterModule(view: MusicPlayContract.View, trackIds: List<Long>, trackPosition: Int) = Kodein.Module {
    bind<MusicPlayContract.View>() with instance(view)
    bind<List<Long>>("TrackIds") with instance(trackIds)
    bind<Int>("TrackPosition") with instance(trackPosition)
}