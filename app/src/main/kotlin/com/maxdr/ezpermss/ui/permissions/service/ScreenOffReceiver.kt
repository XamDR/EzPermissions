package com.maxdr.ezpermss.ui.permissions.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.maxdr.ezpermss.util.debug

class ScreenOffReceiver(private val action: () -> Unit) : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
		if (intent.action == Intent.ACTION_SCREEN_OFF) {
			debug("RECEIVER", "Screen off")
			action()
		}
	}
}