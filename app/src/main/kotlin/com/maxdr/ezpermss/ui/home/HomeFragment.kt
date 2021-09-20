package com.maxdr.ezpermss.ui.home

import android.os.Bundle
//import android.os.Process
//import android.os.UserHandle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxdr.ezpermss.databinding.HomeFragmentBinding
import com.maxdr.ezpermss.ui.apps.AppListFragment
import com.maxdr.ezpermss.util.mainActivity

class HomeFragment : Fragment() {

	private var binding: HomeFragmentBinding? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = HomeFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding?.btn?.setOnClickListener { mainActivity.navigate(AppListFragment::class.java.name) }
//		binding?.btnRevoke?.setOnClickListener { revokePermission() }
	}

//	private fun revokePermission() {
//		val pm = requireContext().packageManager
//
////		pm.revokeRuntimePermission("com.example.application", "", myUserHandle())
//		val m = pm::class.java.getDeclaredMethod("revokeRuntimePermission",
//			String::class.java,
//			String::class.java,
//			UserHandle::class.java
//		)
//		m.isAccessible = true
//		m.invoke(pm, "com.maxdr.ezpermss",
//			"android.permission.QUERY_ALL_PACKAGES",
//			Process.myUserHandle()
//		)
//	}
}