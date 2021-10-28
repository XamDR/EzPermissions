package com.maxdr.ezpermss.ui.apps

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class PackageReceiver(private val action: (appFullName: String, added: Boolean) -> Unit) : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action == Intent.ACTION_PACKAGE_ADDED) {
			val packageName = intent.dataString?.removePrefix("package:")
			packageName?.let { action(it, true) }
		}
		else if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
			val packageName = intent.dataString?.removePrefix("package:")
			packageName?.let { action(it, false) }
		}
	}
}