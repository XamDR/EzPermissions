package com.maxdr.ezpermss.ui.onboarding

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maxdr.ezpermss.OnboardingActivity
import com.maxdr.ezpermss.databinding.InfoShizukuFragmentBinding

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
		binding?.installShizuku?.setOnClickListener { installShizuku() }
	}

	private fun installShizuku() {
		val uri = Uri.parse("$URI_MARKET$SHIZUKU")
		val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
			addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
					Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
					Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
		}
		try {
			startActivity(goToMarket)
		}
		catch (e: ActivityNotFoundException) {
			startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("$URI_PLAY_STORE$SHIZUKU")))
		}
	}

	companion object {
		private const val SHIZUKU = "moe.shizuku.privileged.api"
		private const val URI_MARKET = "market://details?id="
		private const val URI_PLAY_STORE = "http://play.google.com/store/apps/details?id="
	}
}