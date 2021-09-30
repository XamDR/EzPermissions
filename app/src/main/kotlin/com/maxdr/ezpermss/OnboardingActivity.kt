package com.maxdr.ezpermss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maxdr.ezpermss.databinding.ActivityOnboardingBinding
import com.maxdr.ezpermss.ui.helpers.PreferencesManager
import com.topjohnwu.superuser.Shell

class OnboardingActivity : AppCompatActivity() {

	private lateinit var binding: ActivityOnboardingBinding
	private lateinit var manager: PreferencesManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityOnboardingBinding.inflate(layoutInflater)
		setContentView(binding.root)
		manager = PreferencesManager(this)
		showOnboardingOrMain()
	}

	private fun showOnboardingOrMain() {
		if (manager.isFirstRun) {
			manager.isFirstRun = false
		}
		else {
			goToMainActivity()
		}
	}

	fun goToMainActivity() {
		Shell.getShell {
			startActivity(Intent(this, MainActivity::class.java))
		}
	}

	companion object {
		init {
			Shell.enableVerboseLogging = BuildConfig.DEBUG
			Shell.setDefaultBuilder(Shell.Builder.create()
				.setFlags(Shell.FLAG_REDIRECT_STDERR)
				.setTimeout(10)
			)
		}
	}
}