package jp.zliandroid.mymusicplayer.activity

import android.Manifest
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import jp.zliandroid.mymusicplayer.Album
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.RuntimePermissionUtils
import jp.zliandroid.mymusicplayer.Track
import jp.zliandroid.mymusicplayer.adapter.MyFragmentPagerAdapter
import jp.zliandroid.mymusicplayer.fragments.AlbumListFragment
import jp.zliandroid.mymusicplayer.fragments.TabFragment
import jp.zliandroid.mymusicplayer.fragments.TrackListFragment
import kotlinx.android.synthetic.main.activity_wakeup.*
import kotlinx.android.synthetic.main.app_bar_wakeup.*
import kotlinx.android.synthetic.main.content_wakeup.*

const val PERMISSION_REQUEST_CODE = 1

class WakeupActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, TabFragment.TabFragmentListener, AlbumListFragment.AlbumListFragmentListener, TrackListFragment.TrackListFragmentListener{

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickListItem(track: Track) {
        Toast.makeText(this,"Clicked track",Toast.LENGTH_SHORT).show()
    }

    override fun onClickListItem(album: Album) {
        Toast.makeText(this,"Clicked album",Toast.LENGTH_SHORT).show()
        trackListFragment = TrackListFragment()
        val args = Bundle()
        args.putLong("albumId",album.albumId)
        fragmentTransaction = mFragmentManager.beginTransaction()
        trackListFragment.arguments = args
        fragmentTransaction.hide(tabFragment)
        fragmentTransaction.add(R.id.fragment_container,trackListFragment)
        fragmentTransaction.commit()
    }

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var tabFragment: TabFragment
    private lateinit var trackListFragment: TrackListFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wakeup)
        mFragmentManager = supportFragmentManager
        checkPermission()
        setSupportActionBar(toolbar)
        Log.d("debug","start")

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


    }

    private fun setupFragment(){
        fragmentTransaction = mFragmentManager.beginTransaction()
        tabFragment = TabFragment()
        fragmentTransaction.add(R.id.fragment_container, tabFragment)
        //trackListFragment = TrackListFragment.newInstance(33 )
        //val args = Bundle()
        //args.putLong("albumId",33)
        //fragmentTransaction.add(R.id.fragment_container,trackListFragment)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.wakeup, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_CODE && grantResults.size > 0){
            if (RuntimePermissionUtils.checkGrantResults(*grantResults)) {
                setupFragment()
            } else {
                Toast.makeText(this, "権限ないです", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkPermission(){
        if (RuntimePermissionUtils.hasSelfPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            setupFragment()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)

            }
        }
    }
}