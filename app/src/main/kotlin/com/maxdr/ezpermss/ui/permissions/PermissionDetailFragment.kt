package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.maxdr.ezpermss.databinding.PermissionDetailFragmentBinding

class PermissionDetailFragment : Fragment() {

	private var binding: PermissionDetailFragmentBinding? = null
	private val viewModel by viewModels<PermissionDetailViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = PermissionDetailFragmentBinding.inflate(layoutInflater, container, false).apply {
			lifecycleOwner = this@PermissionDetailFragment.viewLifecycleOwner
			viewModel = this@PermissionDetailFragment.viewModel
		}
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}