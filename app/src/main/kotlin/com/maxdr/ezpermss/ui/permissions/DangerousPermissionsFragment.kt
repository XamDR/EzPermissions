package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.databinding.DangerousPermissionsFragmentBinding

class DangerousPermissionsFragment : Fragment() {

	private var binding: DangerousPermissionsFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = DangerousPermissionsFragmentBinding.inflate(layoutInflater, container, false).apply {
			lifecycleOwner = this@DangerousPermissionsFragment.viewLifecycleOwner
			viewModel = this@DangerousPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.dangerousPermissions.observe(viewLifecycleOwner) {
			binding?.recyclerView?.adapter = DangerousPermissionAdapter(it)
			viewModel.isEmpty.value = it.isEmpty()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}