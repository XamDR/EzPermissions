package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.maxdr.ezpermss.core.PermissionManager
import com.maxdr.ezpermss.databinding.HomeFragmentBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

	private var binding: HomeFragmentBinding? = null
	private lateinit var manager: PermissionManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		manager = PermissionManager(requireContext())
		lifecycleScope.launch {
			manager.insertAppPermissionsInfo()
		}
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = HomeFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

}