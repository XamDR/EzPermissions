package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.maxdr.ezpermss.databinding.OnboardingFragmentBinding

class OnboardingFragment : Fragment() {

	private var binding: OnboardingFragmentBinding? = null
	private lateinit var adapter: OnboardingStateAdapter

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
	}

	private fun setupViewPagerWithTabLayout() {
		adapter = OnboardingStateAdapter(this, listOf(HomeFragment(), InfoShizukuFragment()))
		binding?.onboardingPager?.adapter = adapter
		TabLayoutMediator(binding?.tabDots!!, binding?.onboardingPager!!) { _, _ -> }.attach()
	}
}