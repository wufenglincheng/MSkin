package com.example.mskindemo.ui.index

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.darkliu.mskin.SkinManager
import com.example.mskindemo.R
import com.google.android.material.tabs.TabLayout
import com.example.mskindemo.base.BaseSkinFragment
import kotlinx.android.synthetic.main.fragment_index.*

/**
 * Created by liuting on 18/8/2.
 */
val TAB_ITEMS = arrayOf("热点", "实时", "社会", "游戏", "美图", "金融", "房产", "互联网", "视频", "音乐")

class IndexFragment : BaseSkinFragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_index, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tabLayout.setupWithViewPager(viewPager)
    TAB_ITEMS.forEach {
      tabLayout.addTab(tabLayout.newTab())
    }
    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    val indexPagerAdapter = IndexPagerAdapter(TAB_ITEMS.size, childFragmentManager)
    viewPager.adapter = indexPagerAdapter
    viewPager.currentItem = 1
  }

  override fun onThemeChange() {
    tabLayout.setTabTextColors(SkinManager.resource().getColor(context,R.color.skin_title_color),
      SkinManager.resource().getColor(context,R.color.skin_focus))
    tabLayout.setSelectedTabIndicatorColor(SkinManager.resource().getColor(context,R.color.skin_focus))
  }

}

class IndexPagerAdapter(val size: Int, childFragmentManager: FragmentManager) : FragmentStatePagerAdapter(childFragmentManager) {

  val map: HashMap<Int, Fragment> = hashMapOf()
  override fun getItem(position: Int): Fragment {
    var fragment = map[position]
    if (fragment == null) {
      fragment = FeedListFragment()
      map[position] = fragment
    }
    return fragment
  }

  override fun getCount(): Int {
    return size
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return TAB_ITEMS[position]
  }

}