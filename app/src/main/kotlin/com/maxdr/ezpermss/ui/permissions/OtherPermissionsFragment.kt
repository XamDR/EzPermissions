package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.databinding.OtherPermissionsFragmentBinding
import com.maxdr.ezpermss.util.toBulletedList

class OtherPermissionsFragment : Fragment() {

	private var binding: OtherPermissionsFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = OtherPermissionsFragmentBinding.inflate(inflater, container, false).apply {
			lifecycleOwner = this@OtherPermissionsFragment.viewLifecycleOwner
			viewModel = this@OtherPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showSystemPermissions()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	private fun showSystemPermissions() {
		viewModel.otherPermissions.observe(viewLifecycleOwner) {
			if (it.isEmpty()) {
				binding?.otherPermissions?.text = getString(R.string.no_other_permissions)
				binding?.otherPermissions?.gravity = Gravity.CENTER_HORIZONTAL
			}
			else {
				binding?.otherPermissions?.text = it.toBulletedList()
			}
		}
	}
}