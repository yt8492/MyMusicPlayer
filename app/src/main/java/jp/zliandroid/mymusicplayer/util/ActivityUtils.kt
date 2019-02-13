package jp.zliandroid.mymusicplayer.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

object ActivityUtils {
    fun addFragmentToActivity(fragmentManager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }
}