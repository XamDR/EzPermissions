package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.databinding.FragmentNondangerousPermissionsBinding

class NonDangerousPermissionsFragment : Fragment() {

	private var binding: FragmentNondangerousPermissionsBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = FragmentNondangerousPermissionsBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@NonDangerousPermissionsFragment.viewLifecycleOwner
			viewModel = this@NonDangerousPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showNonDangerousPermissions()
	}

	private fun showNonDangerousPermissions() {
		viewModel.nonDangerousPermissions.observe(viewLifecycleOwner) {
			binding?.recyclerView?.adapter = NonDangerousPermissionAdapter(it)
			viewModel.isEmpty.value = it.isEmpty()
		}
	}
}