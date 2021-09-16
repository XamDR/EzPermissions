package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdr.ezpermss.databinding.DangerousPermissionsFragmentBinding

class DangerousPermissionsFragment : Fragment() {

	private var binding: DangerousPermissionsFragmentBinding? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = DangerousPermissionsFragmentBinding.inflate(layoutInflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}