package jp.zliandroid.mymusicplayer.musicplay

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

class MusicPlayActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var musicPlayPresenter: MusicPlayContract.Presenter

    val albumId by lazy {
        intent.getLongExtra("AlbumId", -1)
    }

    val trackIds by lazy {
        intent.getLongArrayExtra("TrackIds")
                .toList()
    }

    val trackPosition by lazy {
        intent.getIntExtra("TrackPosition", -1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)
        AndroidInjection.inject(this)

        supportFragmentManager.findFragmentById(R.id.fragment_music_play_container) as? MusicPlayFragment
                ?: MusicPlayFragment.newInstance().apply {
                    ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_music_play_container)
                }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
