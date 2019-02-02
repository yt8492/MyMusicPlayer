package jp.zliandroid.mymusicplayer.tracklist

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance

fun trackListPresenterModule(view: TrackListContract.View, albumId: Long) = Kodein.Module {
    bind<TrackListContract.View>() with instance(view)
    bind<Long>("AlbumId") with instance(albumId)
}