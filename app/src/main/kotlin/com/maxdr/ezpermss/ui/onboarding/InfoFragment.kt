package com.maxdr.ezpermss.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxdr.ezpermss.OnboardingActivity
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.databinding.InfoFragmentBinding

class InfoFragment : Fragment() {

	private var binding: InfoFragmentBinding? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View? {
		binding = InfoFragmentBinding.inflate(inflater, container, false)
		return binding?.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		binding = null
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		goToMainActivity()
		binding?.appFeatures?.setText(R.string.features)
	}

	private fun goToMainActivity() {
		binding?.next?.setOnClickListener {
			(activity as OnboardingActivity).goToMainActivity()
		}
	}
}