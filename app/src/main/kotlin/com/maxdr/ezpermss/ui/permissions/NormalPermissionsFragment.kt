package com.maxdr.ezpermss.ui.permissions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdr.ezpermss.databinding.NormalPermissionsFragmentBinding

class NormalPermissionsFragment : Fragment() {

	private var binding: NormalPermissionsFragmentBinding? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = NormalPermissionsFragmentBinding.inflate(layoutInflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}