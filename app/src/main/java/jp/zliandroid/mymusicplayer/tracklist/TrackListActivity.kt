package jp.zliandroid.mymusicplayer.tracklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.util.ActivityUtils
import javax.inject.Inject

class TrackListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var trackListPresenter: TrackListContract.Presenter

    val albumId by lazy {
        intent.getLongExtra("AlbumId", -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_list)
        AndroidInjection.inject(this)

        supportFragmentManager.findFragmentById(R.id.fragment_track_list_container) as? TrackListFragment
            ?: TrackListFragment.newInstance(albumId).apply {
                ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_track_list_container)
            }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
