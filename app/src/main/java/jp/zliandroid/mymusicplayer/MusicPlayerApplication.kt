package jp.zliandroid.mymusicplayer

import android.app.Application
import com.github.salomonbrys.kodein.*
import jp.zliandroid.mymusicplayer.data.albumsource.AlbumRepository
import jp.zliandroid.mymusicplayer.data.tracksource.TrackRepository

class MusicPlayerApplication : Application(), KodeinAware {
    override val kodein: Kodein by Kodein.lazy {
        import(applicationModule(this@MusicPlayerApplication))
        bind<AlbumRepository>() with singleton { AlbumRepository(instance()) }
        bind<TrackRepository>() with singleton { TrackRepository(instance()) }
    }
}