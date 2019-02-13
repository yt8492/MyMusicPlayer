package jp.zliandroid.mymusicplayer.albumlist

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.appKodein
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.util.ActivityUtils
import kotlinx.android.synthetic.main.toolbar.*

const val PERMISSION_CODE = 100
class AlbumListActivity : AppCompatActivity() {

    private val injector = KodeinInjector()
    private val albumListPresenter: AlbumListContract.Presenter by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)
        setSupportActionBar(toolbar)
        setupPermission()

        val albumListFragment = supportFragmentManager.findFragmentById(R.id.fragment_album_list_container) as? AlbumListFragment
                ?: AlbumListFragment.newInstance().apply {
                    Log.d("debug", "apply")
                    ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_album_list_container)
                }

        injector.inject(Kodein {
            extend(appKodein())
            import(albumListPresenterModule(albumListFragment))
            bind<AlbumListContract.Presenter>() with provider { AlbumListPresenter(instance(), instance()) }
        })
    }

    private fun setupPermission() {
        val permissionList = mutableListOf<String>()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), PERMISSION_CODE)
        }
    }
}
