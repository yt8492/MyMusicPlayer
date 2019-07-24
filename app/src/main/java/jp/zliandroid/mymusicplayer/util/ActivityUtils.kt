package jp.zliandroid.mymusicplayer.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object ActivityUtils {
    fun addFragmentToActivity(fragmentManager: androidx.fragment.app.FragmentManager, fragment: androidx.fragment.app.Fragment, frameId: Int) {
        val transaction = fragmentManager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commit()
    }
}