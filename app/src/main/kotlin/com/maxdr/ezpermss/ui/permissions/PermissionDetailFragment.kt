package com.maxdr.ezpermss.ui.permissions

import android.content.pm.PermissionInfo.PROTECTION_DANGEROUS
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
import com.maxdr.ezpermss.core.AppInfoPermissions
import com.maxdr.ezpermss.databinding.FragmentPermissionDetailBinding
import com.maxdr.ezpermss.ui.permissions.schedule.RevokePermissionWorker
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.util.mainActivity
import java.util.concurrent.TimeUnit

class PermissionDetailFragment : Fragment() {

	private var binding: FragmentPermissionDetailBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel>{
		PermissionDetailViewModelFactory(appInfoPermissions?.appInfo?.fullName!!)
	}
	private var appInfoPermissions: AppInfoPermissions? = null
	private lateinit var adapter: PermissionStateAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		appInfoPermissions = arguments?.getParcelable("info")
		debug("APP_INFO", appInfoPermissions)
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentPermissionDetailBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@PermissionDetailFragment.viewLifecycleOwner
			appInfo = this@PermissionDetailFragment.appInfoPermissions?.appInfo
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

	fun setupWorker(position: Int, delay: Long) {
		val packageName = appInfoPermissions?.appInfo?.fullName
		val permissionName = getPermissionName(position)
		val workerData = workDataOf("PACKAGE_NAME" to packageName, "PERMISSION_NAME" to permissionName)

		val request = OneTimeWorkRequestBuilder<RevokePermissionWorker>()
			.setInputData(workerData)
			.setInitialDelay(delay, TimeUnit.MINUTES)
			.build()
		WorkManager.getInstance(requireContext()).enqueue(request)
	}

	private fun getPermissionName(position: Int): String? {
		return appInfoPermissions?.permissions?.filter {
			(it.protectionLevel or PROTECTION_DANGEROUS) == it.protectionLevel
		}?.get(position)?.name
	}
}