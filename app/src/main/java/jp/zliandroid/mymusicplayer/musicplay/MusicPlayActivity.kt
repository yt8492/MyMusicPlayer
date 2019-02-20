//package jp.zliandroid.mymusicplayer.musicplay
//
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import com.github.salomonbrys.kodein.*
//import com.github.salomonbrys.kodein.android.appKodein
//import jp.zliandroid.mymusicplayer.R
//import jp.zliandroid.mymusicplayer.util.ActivityUtils
//
//class MusicPlayActivity : AppCompatActivity() {
//
//    private val injector = KodeinInjector()
//    private val musicPlayPresenter: MusicPlayContract.Presenter by injector.instance()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_music_play)
//
//        val albumId = intent.getLongExtra("AlbumId", -1)
//        val trackIds = intent.getLongArrayExtra("TrackIds")
//        val trackPosition = intent.getIntExtra("TrackPosition", -1)
//
//        val musicPlayFragment = supportFragmentManager.findFragmentById(R.id.fragment_music_play_container) as? MusicPlayFragment
//                ?: MusicPlayFragment.newInstance().apply {
//                    ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_music_play_container)
//                }
//
//        injector.inject(Kodein {
//            extend(appKodein())
//            import(musicPlayPresenterModule(musicPlayFragment))
//            bind<MusicPlayContract.Presenter>() with provider { MusicPlayPresenter(albumId, trackIds.toList(), trackPosition, instance(), instance(), instance()) }
//        })
//    }
//}
