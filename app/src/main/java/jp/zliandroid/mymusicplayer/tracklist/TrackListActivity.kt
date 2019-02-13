package jp.zliandroid.mymusicplayer.tracklist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.util.ActivityUtils

class TrackListActivity : AppCompatActivity() {

    private val injector = KodeinInjector()
    private val trackListPresenter: TrackListContract.Presenter by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)

        val albumId = intent.getLongExtra("AlbumId", -1 )

        val trackListFragment = supportFragmentManager.findFragmentById(R.id.fragment_track_list_container) as? TrackListFragment
            ?: TrackListFragment.newInstance(albumId).apply {
                ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_track_list_container)
            }

        injector.inject(Kodein {
            extend(appKodein())
            import(trackListPresenterModule(trackListFragment))
            bind<TrackListContract.Presenter>() with provider { TrackListPresenter(albumId, instance(), instance(), instance()) }
        })
    }
}
