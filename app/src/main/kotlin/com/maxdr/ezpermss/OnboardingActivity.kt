package com.maxdr.ezpermss

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maxdr.ezpermss.core.PermissionManager
import com.maxdr.ezpermss.databinding.ActivityOnboardingBinding
import com.maxdr.ezpermss.ui.helpers.PreferencesManager
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

	private lateinit var binding: ActivityOnboardingBinding
	private lateinit var preferencesManager: PreferencesManager
	private lateinit var permissionManager: PermissionManager

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityOnboardingBinding.inflate(layoutInflater)
		setContentView(binding.root)
		preferencesManager = PreferencesManager(this)
		permissionManager = PermissionManager(this)
		showOnboardingOrMain()
	}

	private fun showOnboardingOrMain() {
		if (preferencesManager.isFirstRun) {
			lifecycleScope.launch {
				permissionManager.insertAppPermissionsInfo()
			}
			preferencesManager.isFirstRun = false
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