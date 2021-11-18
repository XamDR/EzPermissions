package com.maxdr.ezpermss.ui.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.maxdr.ezpermss.core.PackageManagerHelper
import com.maxdr.ezpermss.databinding.OnboardingFragmentBinding
import com.maxdr.ezpermss.ui.apps.AppListFragment
import com.maxdr.ezpermss.ui.helpers.PreferencesManager
import com.maxdr.ezpermss.util.debug
import com.maxdr.ezpermss.util.mainActivity
import kotlinx.coroutines.launch

class OnboardingFragment : Fragment() {

	private var binding: OnboardingFragmentBinding? = null
	private lateinit var adapter: OnboardingStateAdapter
	private lateinit var preferencesManager: PreferencesManager

	override fun onAttach(context: Context) {
		super.onAttach(context)
		preferencesManager = PreferencesManager(context)
	}

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = OnboardingFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViewPagerWithTabLayout()
		insertData()
	}

	private fun setupViewPagerWithTabLayout() {
		adapter = OnboardingStateAdapter(this, listOf(HomeFragment(), InfoShizukuFragment()))
		binding?.onboardingPager?.adapter = adapter
		TabLayoutMediator(binding?.tabDots!!, binding?.onboardingPager!!) { _, _ -> }.attach()
	}

	private fun insertData() {
		if (preferencesManager.isFirstRun) {
			viewLifecycleOwner.lifecycleScope.launch {
				PackageManagerHelper(requireContext()).insertAppsInfo()
				PackageManagerHelper(requireContext()).insertDangerousPermissions()
				debug("EZPERMSS", "Insertion done")
			}
			preferencesManager.isFirstRun = false
		}
		else {
			mainActivity.navigate(AppListFragment::class.java.name, isMainDestination = true)
		}
	}
}