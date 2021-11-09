package com.maxdr.ezpermss.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.maxdr.ezpermss.R
import com.maxdr.ezpermss.util.installShizuku
import com.maxdr.ezpermss.util.setNightMode

class SettingsFragment : PreferenceFragmentCompat() {

	private val themeListener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
		when (key) {
			"app_theme" -> setNightMode(preferences)
		}
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		setPreferencesFromResource(R.xml.root_preferences, rootKey)
		findPreference<Preference>("install_shizuku")?.setOnPreferenceClickListener {
			installShizuku(this); true
		}
	}

	override fun onPause() {
		super.onPause()
		preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(themeListener)
	}

	override fun onResume() {
		super.onResume()
		preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(themeListener)
	}
}