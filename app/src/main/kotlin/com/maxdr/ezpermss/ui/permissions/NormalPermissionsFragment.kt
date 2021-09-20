package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.databinding.NormalPermissionsFragmentBinding
import com.maxdr.ezpermss.util.toBulletedList

class NormalPermissionsFragment : Fragment() {

	private var binding: NormalPermissionsFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel> ( { requireParentFragment() } )

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = NormalPermissionsFragmentBinding.inflate(layoutInflater, container, false).apply {
			lifecycleOwner = this@NormalPermissionsFragment.viewLifecycleOwner
			viewModel = this@NormalPermissionsFragment.viewModel
		}
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.normalPerssions.observe(viewLifecycleOwner) {
			if (it.isEmpty()) {
				binding?.normalPermissions?.text = getString(R.string.no_normal_permissions)
				binding?.normalPermissions?.gravity = Gravity.CENTER
			}
			else {
				binding?.normalPermissions?.text = it.toBulletedList()
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}