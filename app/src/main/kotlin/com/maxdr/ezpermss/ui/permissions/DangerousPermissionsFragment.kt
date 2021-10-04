package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.core.DangerousPermissionInfo
import com.maxdr.ezpermss.databinding.DangerousPermissionsFragmentBinding
import com.maxdr.ezpermss.ui.permissions.schedule.TimePickerDialogFragment

class DangerousPermissionsFragment : Fragment() {

	private var binding: DangerousPermissionsFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )
	private lateinit var adapter: DangerousPermissionAdapter

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = DangerousPermissionsFragmentBinding.inflate(layoutInflater, container, false).apply {
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
		viewModel.dangerousPermissions.observe(viewLifecycleOwner) {
			adapter = DangerousPermissionAdapter(it)
			binding?.recyclerView?.adapter = adapter
			viewModel.isEmpty.value = it.isEmpty()
			adapter.setOnPermissionToggledListener { checked, position ->
				toggleDangerousPermissionStatus(it[position], checked)
			}
			adapter.setOnPermissionRevokedListener { position ->
				showTimePickerDialogFragment(it[position])
			}
		}
	}

	private fun showTimePickerDialogFragment(dangerousPermission: DangerousPermissionInfo) {
		val dialog = TimePickerDialogFragment.newInstance(dangerousPermission)
		dialog.show(parentFragmentManager, "TIME_DIALOG")
	}

	private fun toggleDangerousPermissionStatus(dangerousPermission: DangerousPermissionInfo, checked: Boolean) {
		val permissionName = dangerousPermission.realName

		if (checked) {
			viewModel.grantDangerousPermission(permissionName)
		}
		else {
			viewModel.revokeDangerousPermission(permissionName)
		}
	}
}