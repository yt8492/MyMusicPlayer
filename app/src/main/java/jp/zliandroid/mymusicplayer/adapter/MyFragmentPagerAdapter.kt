package jp.zliandroid.mymusicplayer.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import jp.zliandroid.mymusicplayer.fragments.AlbumListFragment
import jp.zliandroid.mymusicplayer.fragments.FilerFragment

class MyFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val pageTitles = listOf<CharSequence>("アルバム","フォルダ")
    private val fragments = listOf<Fragment>(AlbumListFragment(),FilerFragment())

    override fun getCount(): Int {
        return pageTitles.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pageTitles[position]
    }
}