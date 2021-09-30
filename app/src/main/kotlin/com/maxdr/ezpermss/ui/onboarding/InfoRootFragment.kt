package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maxdr.ezpermss.OnboardingActivity
import com.maxdr.ezpermss.databinding.InfoRootFragmentBinding

class InfoRootFragment : Fragment() {

	private var binding: InfoRootFragmentBinding? = null

	override fun onCreateView(inflater: LayoutInflater,
							  container: ViewGroup?,
							  savedInstanceState: Bundle?): View? {
		binding = InfoRootFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		goToMainActivity()
	}

	private fun goToMainActivity() {
		binding?.next?.setOnClickListener {
			(activity as OnboardingActivity).goToMainActivity()
		}
	}
}