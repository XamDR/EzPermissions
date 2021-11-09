package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxdr.ezpermss.ui.activities.OnboardingActivity
import com.maxdr.ezpermss.databinding.InfoShizukuFragmentBinding
import com.maxdr.ezpermss.util.installShizuku

class InfoShizukuFragment : Fragment() {

	private var binding: InfoShizukuFragmentBinding? = null

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = InfoShizukuFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding?.skip?.setOnClickListener { (activity as OnboardingActivity).goToMainActivity() }
		binding?.installShizuku?.setOnClickListener { installShizuku(this) }
	}
}