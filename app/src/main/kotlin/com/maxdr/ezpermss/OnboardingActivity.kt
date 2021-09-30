package com.maxdr.ezpermss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxdr.ezpermss.databinding.ActivityOnboardingBinding
import com.maxdr.ezpermss.ui.helpers.PreferencesManager

class OnboardingActivity : AppCompatActivity() {

	private lateinit var binding: ActivityOnboardingBinding
	private lateinit var manager: PreferencesManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityOnboardingBinding.inflate(layoutInflater)
		setContentView(binding.root)
		manager = PreferencesManager(this)
		showIntroOrMain()
	}

	private fun showIntroOrMain() {
		if (manager.isFirstRun) {
			manager.isFirstRun = false
		}
		else {
			goToMainActivity()
		}
	}

	fun goToMainActivity() = startActivity(Intent(this, MainActivity::class.java))
}