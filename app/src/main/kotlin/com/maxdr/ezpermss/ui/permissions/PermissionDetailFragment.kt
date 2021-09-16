package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.databinding.PermissionDetailFragmentBinding
import com.maxdr.ezpermss.util.debug

class PermissionDetailFragment : Fragment() {

	private var binding: PermissionDetailFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> {
		PermissionDetailViewModelFactory(requireActivity().application, appInfo?.packageFullName!!)
	}
	private var appInfo: AppInfo? = null
	private lateinit var adapter: PermissionsAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		appInfo = arguments?.getParcelable("info")
		debug("APP_INFO", appInfo)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = PermissionDetailFragmentBinding.inflate(layoutInflater, container, false).apply {
			lifecycleOwner = this@PermissionDetailFragment.viewLifecycleOwner
			appInfo = this@PermissionDetailFragment.appInfo
			viewModel = this@PermissionDetailFragment.viewModel
		}
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViewPagerWithTabLayout()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	private fun setupViewPagerWithTabLayout() {
		adapter = PermissionsAdapter(this, listOf(NormalPermissionsFragment(), DangerousPermissionsFragment()))
		binding?.pager?.adapter = adapter
		TabLayoutMediator(binding?.tabLayout!!, binding?.pager!!) { tab, position ->
			tab.text = if (position == 0) getString(R.string.normal) else getString(R.string.dangerous)
		}.attach()
	}
}