package jp.zliandroid.mymusicplayer.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import android.widget.Toast
import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.RuntimePermissionUtils
import jp.zliandroid.mymusicplayer.service.MusicPlayService
import jp.zliandroid.mymusicplayer.fragments.AlbumListFragment
import jp.zliandroid.mymusicplayer.fragments.PlayerFragment
import jp.zliandroid.mymusicplayer.fragments.TabFragment
import jp.zliandroid.mymusicplayer.fragments.TrackListFragment
import kotlinx.android.synthetic.main.activity_wakeup.*
import kotlinx.android.synthetic.main.app_bar_wakeup.*

const val PERMISSION_REQUEST_CODE = 1

class WakeupActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AlbumListFragment.AlbumListFragmentListener,
        TrackListFragment.TrackListFragmentListener, PlayerFragment.PlayerFragmentListener{

    private lateinit var mFragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var tabFragment: TabFragment
    private lateinit var trackListFragment: TrackListFragment
    private lateinit var playerFragment: PlayerFragment

    override fun onButtonClick(controlType: Int) {
        Log.d("debug", "onButtonClick")
        val intent = Intent(this, MusicPlayService::class.java)
        intent.action = ACTION_SEND_CONTROL
        intent.putExtra("type", controlType)
        startService(intent)
    }

    override fun onClickListItem(albumId: Long, position: Int) {
        //vToast.makeText(this,"albumId = $albumId, position = $position",Toast.LENGTH_SHORT).show()
        startService(albumId,position)

        playerFragment = PlayerFragment.newInstance(albumId, position)
        fragmentTransaction = mFragmentManager.beginTransaction()
        fragmentTransaction.hide(trackListFragment)
        fragmentTransaction.add(R.id.fragment_container, playerFragment, PlayerFragment.NAME)
        fragmentTransaction.addToBackStack(PlayerFragment.NAME)
        fragmentTransaction.commitAllowingStateLoss()

    }

    override fun onClickListItem(albumId: Long) {
        trackListFragment = TrackListFragment()
        val args = Bundle()
        args.putLong("albumId",albumId)
        fragmentTransaction = mFragmentManager.beginTransaction()
        trackListFragment.arguments = args
        fragmentTransaction.hide(tabFragment)
        fragmentTransaction.add(R.id.fragment_container, trackListFragment, TrackListFragment.NAME)
        fragmentTransaction.addToBackStack(TrackListFragment.NAME)
        fragmentTransaction.commitAllowingStateLoss()
    }

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
        fragmentTransaction.add(R.id.fragment_container, tabFragment, TabFragment.NAME)
        fragmentTransaction.addToBackStack(TabFragment.NAME)
        fragmentTransaction.commit()

    }

    override fun onBackPressed() {
        when {
            drawer_layout.isDrawerOpen(GravityCompat.START) -> drawer_layout.closeDrawer(GravityCompat.START)
            mFragmentManager.backStackEntryCount == 1 -> finish()
            else -> super.onBackPressed()
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
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
        if(requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()){
            if (RuntimePermissionUtils.checkGrantResults(*grantResults)) {
                setupFragment()
            } else {
                Toast.makeText(this, "権限ないです", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun startService(albumId: Long, position: Int){
        val intent = Intent(this, MusicPlayService::class.java)
        intent.action = ACTION_CONNECT_SERVICE
        intent.putExtra("albumId", albumId)
        intent.putExtra("position", position)
        this.startService(intent)
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

    companion object {
        const val ACTION_CONNECT_SERVICE = "android.intent.action.CONNECT_SERVICE"
        const val ACTION_SEND_CONTROL = "android.intent.action.SEND_CONTROL"
    }
}