package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.core.PermissionInfo
import com.maxdr.ezpermss.databinding.FragmentDangerousPermissionsBinding

class DangerousPermissionsFragment : Fragment() {

	private var binding: FragmentDangerousPermissionsBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )
	private lateinit var adapter: DangerousPermissionAdapter
	private lateinit var helper: PermissionHelper

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		helper = PermissionHelper(requireContext())
	}

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
			}
			viewModel.hasDangerousPermissions.value = it.isEmpty()
		}
	}

	private fun toggleDangerousPermissionStatus(grant: Boolean, permissionInfo: PermissionInfo) {
		if (grant) {
			helper.grantDangerousPermission(viewModel.appFullName, permissionInfo.name)
		}
		else {
			helper.revokeDangerousPermission(viewModel.appFullName, permissionInfo.name)
		}
	}
}