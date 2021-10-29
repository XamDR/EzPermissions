package com.maxdr.ezpermss.ui.permissions.dangerous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.data.AppRepository
import com.maxdr.ezpermss.databinding.FragmentDangerousPermissionsBinding
import com.maxdr.ezpermss.ui.helpers.PreferencesManager
import com.maxdr.ezpermss.ui.permissions.PermissionDetailFragment
import com.maxdr.ezpermss.ui.permissions.PermissionDetailViewModel
import com.maxdr.ezpermss.ui.permissions.PermissionHelper
import kotlinx.coroutines.launch

class DangerousPermissionsFragment : Fragment() {

	private var binding: FragmentDangerousPermissionsBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )
	private lateinit var adapter: DangerousPermissionAdapter
	private val bottomHeaderAdapter = HeaderDangerousPermissionAdapter(mostUsed = false)
	private val topHeaderAdapter = HeaderDangerousPermissionAdapter(mostUsed = true)

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentDangerousPermissionsBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@DangerousPermissionsFragment.viewLifecycleOwner
			viewModel = this@DangerousPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showDangerousPermissions()
	}

	private fun showDangerousPermissions() {
		val manager = PreferencesManager(requireContext())
		binding?.topRecyclerView?.adapter = ConcatAdapter(topHeaderAdapter)

		if (manager.isServiceRunning) {
			viewModel.dangerousPermissionsFromDb.observe(viewLifecycleOwner) {
				adapter = DangerousPermissionAdapter(it).apply {
					val concatAdapter = ConcatAdapter(bottomHeaderAdapter, this)
					binding?.bottomRecyclerView?.adapter = concatAdapter
					setOnPermissionToggledListener { checked, position ->
						toggleDangerousPermissionStatusDb(checked, it[position])
					}
					setOnPermissionRevokedListener { position, delay ->
						revokeDangerousPermissionAfterDelay(it[position], delay)
					}
				}
				viewModel.hasDangerousPermissions.value = it.isEmpty()
			}
		}
		else {
			viewModel.dangerousPermissions.observe(viewLifecycleOwner) {
				adapter = DangerousPermissionAdapter(it).apply {
					val concatAdapter = ConcatAdapter(bottomHeaderAdapter, this)
					binding?.bottomRecyclerView?.adapter = concatAdapter
					setOnPermissionToggledListener { checked, position ->
						toggleDangerousPermissionStatus(checked, it[position])
					}
					setOnPermissionRevokedListener { position, delay ->
						revokeDangerousPermissionAfterDelay(it[position], delay)
					}
				}
				viewModel.hasDangerousPermissions.value = it.isEmpty()
			}
		}
	}

	private fun toggleDangerousPermissionStatus(grant: Boolean, dangerousDangerousPermission: DangerousPermissionInfo) {
		if (grant) {
			PermissionHelper.grantDangerousPermission(
				context = requireContext(),
				packageName = viewModel.appFullName,
				permissionName = dangerousDangerousPermission.name
			)
		}
		else {
			PermissionHelper.revokeDangerousPermission(
				context = requireContext(),
				packageName = viewModel.appFullName,
				permissionName = dangerousDangerousPermission.name
			)
		}
	}

	private fun toggleDangerousPermissionStatusDb(grant: Boolean, dangerousDangerousPermission: DangerousPermissionInfo) {
		viewLifecycleOwner.lifecycleScope.launch {
			if (grant) {
				PermissionHelper.grantDangerousPermission(
					context = requireContext(),
					packageName = viewModel.appFullName,
					permissionName = dangerousDangerousPermission.name
				)
			}
			else {
				PermissionHelper.revokeDangerousPermission(
					context = requireContext(),
					packageName = viewModel.appFullName,
					permissionName = dangerousDangerousPermission.name
				)
			}
			AppRepository.Instance.updateDangerousPermissionInfo(
				packageName = viewModel.appFullName,
				permissionName = dangerousDangerousPermission.name,
				granted = grant
			)
		}
	}

	private fun revokeDangerousPermissionAfterDelay(dangerousDangerousPermission: DangerousPermissionInfo, delay: Long) {
		val message = getString(R.string.timeout_message, delay)
		Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
		(requireParentFragment() as PermissionDetailFragment).setupWorker(dangerousDangerousPermission, delay)
	}
}