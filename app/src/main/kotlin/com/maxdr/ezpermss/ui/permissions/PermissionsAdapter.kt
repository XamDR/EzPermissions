package com.maxdr.ezpermss.ui.permissions

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PermissionsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

	override fun getItemCount() = FRAGMENT_COUNT

	override fun createFragment(position: Int): Fragment {
		return when (position) {
			0 -> NormalPermissionsFragment()
			1 -> DangerousPermissionsFragment()
			else -> throw IllegalStateException("Invalid position. $position is greater than $FRAGMENT_COUNT.")
		}
	}

	companion object {
		private const val FRAGMENT_COUNT = 2
	}
}