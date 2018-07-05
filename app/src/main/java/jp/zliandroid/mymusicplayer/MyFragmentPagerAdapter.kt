package jp.zliandroid.mymusicplayer

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MyFragmentPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val tabTitles: Array<CharSequence> = arrayOf("アルバム","フォルダ")
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(AlbumListFragment())
        fragments.add(FilerFragment())
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

}