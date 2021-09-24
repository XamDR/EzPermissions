package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdr.ezpermss.databinding.OnboardingFragmentBinding

class OnboardingFragment : Fragment() {

	private var binding: OnboardingFragmentBinding? = null
	private lateinit var adapter: OnboardingStateAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = OnboardingFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = OnboardingStateAdapter(this, listOf(HomeFragment(), InfoFragment()))
		binding?.onboardingPager?.adapter = adapter
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}
}