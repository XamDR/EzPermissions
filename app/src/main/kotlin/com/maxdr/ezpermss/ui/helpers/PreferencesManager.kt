package com.maxdr.ezpermss.ui.helpers

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PreferencesManager(context: Context) {

	private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

	var isFirstRun = preferences.getBoolean(FIRST_TIME_PREFERENCE, true)
		set(value) = preferences.edit { putBoolean(FIRST_TIME_PREFERENCE, value) }

	companion object {
		private const val FIRST_TIME_PREFERENCE = "isFirstRun"
	}
}