package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.databinding.FragmentDangerousPermissionsBinding

class DangerousPermissionsFragment : Fragment() {

	private var binding: FragmentDangerousPermissionsBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )
	private lateinit var adapter: DangerousPermissionAdapter

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
		viewModel.dangerousPermissions.observe(viewLifecycleOwner) {
			adapter = DangerousPermissionAdapter(it).apply {
				binding?.recyclerView?.adapter = this
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

	private fun toggleDangerousPermissionStatus(grant: Boolean, dangerousDangerousPermission: DangerousPermissionInfo) {
		if (grant) {
			PermissionHelper.grantDangerousPermission(requireContext(), viewModel.appFullName, dangerousDangerousPermission.name)
		}
		else {
			PermissionHelper.revokeDangerousPermission(requireContext(), viewModel.appFullName, dangerousDangerousPermission.name)
		}
	}

	private fun revokeDangerousPermissionAfterDelay(dangerousDangerousPermission: DangerousPermissionInfo, delay: Long) {
		val message = getString(R.string.timeout_message, delay)
		Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
		(requireParentFragment() as PermissionDetailFragment).setupWorker(dangerousDangerousPermission, delay)
	}
}