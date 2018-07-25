package jp.zliandroid.mymusicplayer.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

import jp.zliandroid.mymusicplayer.R
import jp.zliandroid.mymusicplayer.adapter.MyFragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : Fragment() {

    private lateinit var mFragmentManager: FragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tab, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        fragmentManager?.let {
            mFragmentManager = it
            pager.adapter = MyFragmentPagerAdapter(mFragmentManager)
            tabs.setupWithViewPager(pager)

        }
    }


    companion object {

        const val NAME = "TabFragment"
        @JvmStatic
        fun newInstance() = TabFragment()
    }
}
