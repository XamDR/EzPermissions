package com.maxdr.ezpermss.ui.permissions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PermissionsAdapter(fragment: Fragment, private val fragmentList: List<Fragment>) : FragmentStateAdapter(fragment) {

	override fun getItemCount() = fragmentList.size

	override fun createFragment(position: Int) = fragmentList[position]
}