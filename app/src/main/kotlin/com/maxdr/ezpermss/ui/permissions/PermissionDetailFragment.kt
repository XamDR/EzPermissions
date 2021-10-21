package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.material.tabs.TabLayoutMediator
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.AppInfo
import com.maxdr.ezpermss.core.PermissionInfo
import com.maxdr.ezpermss.databinding.FragmentPermissionDetailBinding
import com.maxdr.ezpermss.ui.permissions.schedule.RevokePermissionWorker
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.util.mainActivity
import java.util.concurrent.TimeUnit

class PermissionDetailFragment : Fragment() {

	private var binding: FragmentPermissionDetailBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel>{
		PermissionDetailViewModelFactory(appInfo?.fullName!!)
	}
	private var appInfo: AppInfo? = null
	private lateinit var adapter: PermissionStateAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		appInfo = arguments?.getParcelable("info")
		debug("APP_INFO", appInfo)
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentPermissionDetailBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@PermissionDetailFragment.viewLifecycleOwner
			appInfo = this@PermissionDetailFragment.appInfo
			viewModel = this@PermissionDetailFragment.viewModel
			handler = AppSettingsHandler(mainActivity)
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViewPagerWithTabLayout()
	}

	private fun setupViewPagerWithTabLayout() {
		adapter = PermissionStateAdapter(this,
			listOf(NonDangerousPermissionsFragment(), DangerousPermissionsFragment()))
		binding?.pager?.adapter = adapter
		TabLayoutMediator(binding?.tabLayout!!, binding?.pager!!) { tab, position ->
			tab.text = if (position == 0) getString(R.string.nondangerous_permission)
					   else getString(R.string.dangerous_permission)
		}.attach()
	}

	fun setupWorker(dangerousPermission: PermissionInfo, delay: Long) {
		val packageName = appInfo?.fullName
		val permissionName = dangerousPermission.name
		val workerData = workDataOf("PACKAGE_NAME" to packageName, "PERMISSION_NAME" to permissionName)

		val request = OneTimeWorkRequestBuilder<RevokePermissionWorker>()
			.setInputData(workerData)
			.setInitialDelay(delay, TimeUnit.MINUTES)
			.build()
		WorkManager.getInstance(requireContext()).enqueue(request)
	}
}