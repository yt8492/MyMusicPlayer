package jp.zliandroid.mymusicplayer.albumlist

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.util.ActivityUtils
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class AlbumListActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    internal lateinit var albumListPresenter: AlbumListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_list)
        AndroidInjection.inject(this)
        setSupportActionBar(toolbar)
        setupPermission()

        supportFragmentManager.findFragmentById(R.id.fragment_album_list_container) as? AlbumListFragment
                ?: AlbumListFragment.newInstance().apply {
                    ActivityUtils.addFragmentToActivity(supportFragmentManager, this, R.id.fragment_album_list_container)
                }
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

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    companion object {
        private const val PERMISSION_CODE = 100
    }
}
